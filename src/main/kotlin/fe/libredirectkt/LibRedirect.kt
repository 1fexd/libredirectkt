package fe.libredirectkt

import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


fun String.querySplit(): MutableMap<String, String> {
    return this.split("&").mapNotNull {
        with(it.split("=")) {
            if (this.size == 2) {
                this[0] to this[1]
            } else null
        }
    }.toMap().toMutableMap()
}

fun Map<String, String?>.makeQuery(): String {
    return this.map {
        if (it.value != null) {
            "${it.key}=${it.value}"
        } else {
            it.key
        }
    }.joinToString("&")
}

private val mapCentreRegex = Regex("@(-?\\d[0-9.]*),(-?\\d[0-9.]*),(\\d{1,2})[.z]")
private val dataLatLngRegex = Regex("!3d(-?[0-9]{1,}.[0-9]{1,})!4d(-?[0-9]{1,}.[0-9]{1,})")
private val placeRegex = Regex("\\/place\\/(.*)\\/")

private fun convertMapCentre(url: URL): Triple<String, String, String>? {
    mapCentreRegex.matchEntire(url.path)?.groupValues?.let {
        val (_, lat, lon, zoom) = it
        return Triple(lat, lon, zoom)
    }

    val query = url.query?.querySplit()
    if (query?.contains("center") == true) {
        query["center"]?.split(",")?.let {
            return Triple(it[0], it[1], query["zoom"] ?: "17")
        }
    }

    return null
}

enum class RedirectFrontend(vararg val keys: String) {
    Beatbump("beatbump") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "$instanceHost${uri.path}?${uri.query}"
                .replace("/watch?v=", "/listen?id=")
                .replace("/channel/", "/artist/")
                .replace("/playlist?list=", "/playlist/VL")
                .replace(Regex("\\/search\\?q=.*")) { result ->
                    result.groupValues[0].replace(
                        "?q=",
                        "/"
                    ) + "?filter=all"
                }
        }
    },
    Hyperpipe("hyperpipe") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "$instanceHost${uri.path}?${uri.query}".replace(Regex("\\/search\\?q=.*")) { result ->
                result.groupValues[0].replace(
                    "?q=",
                    "/"
                )
            }
        }
    },

    //    LbryDesktop("lbryDesktop")
    Searx("searx", "searxng") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "$instanceHost/?${uri.query}"
        }
    },
    Whoogle("whoogle") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "$instanceHost/search?${uri.query}"
        }

    },
    Librex("librex") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "${instanceHost}/search.php?${uri.query}"
        }
    },

    //    Send("send")
    Nitter("nitter") {
        override fun redirect(uri: URI, instanceHost: String): String {
            val query = uri.query.querySplit()
            query.remove("ref_src")
            query.remove("ref_url")

            var search = query.makeQuery()
            if (query.isNotEmpty()) search = "?$search"

            if (uri.host.split(".")[0] === "pbs" || uri.host.split(".")[0] === "video") {
                Regex("(.*)\\?format=(.*)&(.*)").matchEntire(search)?.groupValues?.let { result ->
                    val (_, id, format, extra) = result
                    return "$instanceHost/pic${uri.path}?${query}"
                }

                return "$instanceHost/pic/${uri.path}$search"
            }

            if (uri.path.split("/").contains("tweets")) {
                return "$instanceHost${uri.path.replace("/tweets", "")}$search"
            }

            if (uri.host == "t.co") {
                return "${instanceHost}/t.co${uri.path}"
            }

            return "$instanceHost${uri.path}${search}#m"
        }
    },

    //    Yattee("yattee"),
//    Freetube("freetube"),
    Invidious("invidious", "piped", "pipedMaterial", "cloudtube") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.path.startsWith("/live_chat")) {
                return null
            }
            return "$instanceHost${uri.path}?${uri.query}";
        }
    },
    Poketube("poketube") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.path.startsWith("/live_chat")) {
                return null
            }
            if (uri.path.startsWith("/channel")) {
                val match = Regex("\\/channel\\/(.*)\\/?\$").find(uri.path)

                if (match != null) {
                    val id = match.groupValues[1]
                    return "${instanceHost}/channel?id=${id}?${uri.query}"
                }
            }

            if (Regex("\\/@[a-z]+\\/").containsMatchIn(uri.path)) {
                return instanceHost
            }

            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    SimplyTranslate("simplyTranslate") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "$instanceHost/?${uri.query}"
        }
    },
    LibreTranslate("libreTranslate") {
        override fun redirect(uri: URI, instanceHost: String): String {
            return "$instanceHost/?${uri.query}"
                .replace(Regex("(?<=\\/?)sl"), "source")
                .replace(Regex("(?<=&)tl"), "target")
                .replace(Regex("(?<=&)text"), "q")
        }

    },
    OSM("osm") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            return null
        }
    },
    Facil("facil") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            return null
        }
    },
    LingVa("lingva") {
        override fun redirect(uri: URI, instanceHost: String): String {
            val params = uri.query.querySplit()
            if (params.containsKey("sl") && params.containsKey("tl") && params.containsKey("text"))
                return "${instanceHost}/${params["sl"]}/${params["tl"]}/${params["text"]}"

            return instanceHost
        }
    },
    BreezeWiki("breezeWiki") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            return null
        }
    },
    Rimgo("rimgo") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (Regex("^https?:\\/{2}(?:[im]\\.)?stack\\.").containsMatchIn(uri.toString())) {
                return "$instanceHost/stack${uri.path}?${uri.query}"
            }

            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    LibReddit("libreddit") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            val result = Regex("^(?:(?:external-)?preview|i)(?=\\.redd\\.it)").find(uri.host)
                ?: return "$instanceHost${uri.path}?${uri.query}"

            return when (result.groupValues[1]) {
                "preview" -> "$instanceHost/preview/pre${uri.path}?${uri.query}"
                "external-preview" -> "$instanceHost/preview/external-pre${uri.path}?${uri.query}"
                "i" -> "$instanceHost/img${uri.path}"
                else -> null
            }
        }
    },
    Teddit("teddit") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (Regex("^(?:(?:external-)?preview|i)\\.redd\\.it").containsMatchIn(uri.host)) {
                if (uri.query == "") {
                    return "$instanceHost${uri.path}?teddit_proxy=${uri.host}"
                }

                return "$instanceHost${uri.path}?${uri.query}&teddit_proxy=${uri.host}"
            }

            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    Neuters("neuters") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.path.startsWith("/article/") || uri.path.startsWith("/pf/") || uri.path.startsWith("/arc/") || uri.path.startsWith(
                    "/resizer/"
                )
            ) {
                return null
            }

            return "${instanceHost}${uri.path}"
        }
    },
    Dumb("dumb") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.path.endsWith("-lyrics")) {
                return "${instanceHost}${uri.path}"
            }

            return null
        }
    },
    RuralDictionary("ruralDictionary") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (!uri.path.contains("/define.php") && !uri.path.contains("/random.php") && uri.path != "/") return null
            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    AnonymousOverflow("anonymousOverflow") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (!uri.path.startsWith("/questions") && uri.path != "/") return null
            val threadID = Regex("\\/(\\d+)\\/?\$").find(uri.path)?.groupValues
            if (threadID != null) return "${instanceHost}/questions/${threadID[1]}?${uri.query}"
            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    BiblioReads("biblioReads") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (!uri.path.startsWith("/book/show/") && uri.path != "/") return null
            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    WikiLess("wikiless") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            val queries = uri.query?.querySplit() ?: mutableMapOf()

            val link = "$instanceHost${uri.path}"
            val urlSplit = uri.host.split(".")
            if (urlSplit[0] != "wikipedia" && urlSplit[0] != "www") {
                if (urlSplit[0] == "m") {
                    queries["mobileaction"] = "toggle_view_mobile"
                } else {
                    queries["lang"] = urlSplit[0]
                }
                if (urlSplit[1] == "m") {
                    queries["mobileaction"] = "toggle_view_mobile"
                }
            }
            return "$link?${queries.makeQuery()}#${uri.fragment}"
        }
    },
    ProxiTok("proxiTok") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.path.startsWith("/email")) return null
            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    WayBackClassic("waybackClassic") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            val result = Regex("^\\/\\web\\/[0-9]+\\*\\/(.*)").find(uri.path)?.groupValues
            if (result != null) {
                val link = result[1]
                return "${instanceHost}/cgi-bin/history.cgi?utf8=✓&q=${URLEncoder.encode(link, StandardCharsets.UTF_8)}"
            }

            return instanceHost
        }
    },
    Gothub("gothub") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            val regex = Regex("^\\/(.*)\\/(.*)\\/(?:blob|tree)\\/(.*)\\/(.*)").find(uri.path)?.groupValues
            if (regex != null) {
                val user = regex[1]
                val repo = regex[2]
                val branch = regex[3]
                val path = regex[4]
                return "$instanceHost/file/${user}/${repo}/${branch}/${path}"
            }
            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    MikuIndividious("mikuIndividious") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.host == "bilibili.com" || uri.host == "www.bilibili.com" || uri.host == "b23.tv") {
                return "${instanceHost}${uri.path}?${uri.query}"
            }
            if (uri.host == "space.bilibili.com") {
                return "${instanceHost}/space${uri.path}?${uri.query}"
            }

            return null
        }
    },
    Tent("tent") {
        override fun redirect(uri: URI, instanceHost: String): String? {
            if (uri.host == "bandcamp.com" && uri.path == "/search") {
                val query = uri.query.querySplit()["q"]
                return "${instanceHost}/search.php?query=${URLEncoder.encode(query, StandardCharsets.UTF_8)}"
            }
            if (uri.host.endsWith("bandcamp.com")) {
                val regex = Regex("^(.*)\\.bandcamp\\.com").find(uri.host)?.groupValues ?: return null
                val artist = regex[1]
                if (uri.path == "/") {
                    return "${instanceHost}/artist.php?name=${artist}"
                } else {
                    val regex = Regex("^\\/(.*)\\/(.*)").find(uri.path)?.groupValues
                    if (regex != null) {
                        val type = regex[1]
                        val name = regex[2]
                        return "${instanceHost}/release.php?artist=${artist}&type=${type}&name=${name}"
                    }
                }
            }
            if (uri.host == "f4.bcbits.com") {
                val regex = Regex("\\/img\\/(.*)").find(uri.path)?.groupValues ?: return null
                val image = regex[1]
                return "${instanceHost}/image.php?file=${image}"
            }
            if (uri.host == "t4.bcbits.com") {
                val regex = Regex("\\/stream\\/(.*)\\/(.*)\\/(.*)").find(uri.path)?.groupValues
                if (regex != null) {
                    val directory = regex[1]
                    val format = regex[2]
                    val file = regex[3]
                    val token = uri.query.querySplit()["token"]
                    return "${instanceHost}/audio.php/?directory=${directory}&format=${format}&file=${file}&token=${
                        URLEncoder.encode(
                            token,
                            StandardCharsets.UTF_8
                        )
                    }"
                }
            }

            return null
        }
    };

    abstract fun redirect(uri: URI, instanceHost: String): String?

    companion object {
        fun findFrontendByKey(key: String) = RedirectFrontend.values().find { it.keys.contains(key) }
    }
}

object LibRedirect {
    private val defaultInstances = mapOf(
        "invidious" to listOf("https://inv.vern.cc"),
        "piped" to listOf("https://pipedapi-libre.kavin.rocks"),
        "pipedMaterial" to listOf("https://piped-material.xn--17b.net"),
        "cloudtube" to listOf("https://tube.cadence.moe"),
        "poketube" to listOf("https://poketube.fun"),
        "proxiTok" to listOf("https://proxitok.pabloferreiro.es"),
        "send" to listOf("https://send.vis.ee"),
        "nitter" to listOf("https://nitter.net"),
        "libreddit" to listOf("https://libreddit.spike.codes"),
        "teddit" to listOf("https://teddit.net"),
        "scribe" to listOf("https://scribe.rip"),
        "libMedium" to listOf("https://md.vern.cc"),
        "quetre" to listOf("https://quetre.iket.me"),
        "libremdb" to listOf("https://libremdb.iket.me"),
        "simplyTranslate" to listOf("https://simplytranslate.org"),
        "lingva" to listOf("https://lingva.ml"),
        "searxng" to listOf("https://sx.vern.cc"),
        "rimgo" to listOf("https://rimgo.vern.cc"),
        "librarian" to listOf("https://lbry.vern.cc"),
        "beatbump" to listOf("https://beatbump.ml"),
        "hyperpipe" to listOf("https://hyperpipe.surge.sh"),
        "facil" to listOf(" https://facilmap.org "),
        "osm" to listOf("https://www.openstreetmap.org"),
        "breezeWiki" to listOf("https://breezewiki.com"),
        "neuters" to listOf("https://neuters.de"),
        "dumb" to listOf("https://dm.vern.cc"),
        "ruralDictionary" to listOf("https://rd.vern.cc"),
        "anonymousOverflow" to listOf("https://code.whatever.social"),
        "biblioReads" to listOf("https://biblioreads.ml"),
        "wikiless" to listOf("https://wikiless.org"),
        "suds" to listOf("https://sd.vern.cc"),
        "waybackClassic" to listOf("https://wayback-classic.net"),
        "gothub" to listOf("https://gh.odyssey346.dev"),
        "mikuIndividious" to listOf("https://mikuinv.resrv.org"),
        "tent" to listOf("https://tent.sny.sh"),
        "wolfreeAlpha" to listOf("https://gqq.gitlab.io", "https://uqq.gitlab.io")
    )

    fun redirect(url: String, frontendKey: String, instance: String): String? {
        return RedirectFrontend.findFrontendByKey(frontendKey)?.redirect(URI(url), instance)
    }

    fun findServiceForUrl(url: String, services: List<LibRedirectService>): LibRedirectService? {
        return services.find { service ->
            service.targets.any { target -> target.containsMatchIn(url) }
        }
    }

    fun getDefaultInstanceForFrontend(frontendKey: String): List<String>? {
        return defaultInstances[frontendKey]
    }
}

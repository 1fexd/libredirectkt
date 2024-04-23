package fe.libredirectkt

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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

private fun convertMapCentre(url: UriKt): Triple<String, String, String>? {
    url.path?.let {
        mapCentreRegex.find(it)?.groupValues?.let { groupValues ->
            val (_, lat, lon, zoom) = groupValues
            return Triple(zoom, lat, lon)
        }
    }

    if (url.splitQuery.containsKey("center")) {
        url.splitQuery["center"]!!.split(",").let {
            return Triple(url.splitQuery["zoom"] ?: "17", it[0], it[1])
        }
    }

    return null
}

// browser_extension/src/assets/javascripts
enum class RedirectFrontend(vararg val keys: String) {
    Beatbump("beatbump") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
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
        override fun redirect(uri: UriKt, instanceHost: String): String {
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
        override fun redirect(uri: UriKt, instanceHost: String): String {
            return "$instanceHost/?${uri.query}"
        }
    },
    Whoogle("whoogle") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
            return "$instanceHost/search?${uri.query}"
        }

    },
    Librex("librex") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
            return "${instanceHost}/search.php?${uri.query}"
        }
    },

    //    Send("send")
    Nitter("nitter") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
            val query = uri.splitQuery
            query.remove("ref_src")
            query.remove("ref_url")
            query.remove("t")
            query.remove("s")

            var search = query.makeQuery()
            if (query.isNotEmpty()) search = "?$search"

            if (uri.host?.split(".")?.get(0) === "pbs" || uri.host?.split(".")?.get(0) === "video") {
                Regex("(.*)\\?format=(.*)&(.*)").matchEntire(search)?.groupValues?.let { result ->
                    val (_, id, format, extra) = result
                    return "$instanceHost/pic${uri.path}?${query}"
                }

                return "$instanceHost/pic/${uri.path}$search"
            }

            if (uri.path?.split("/")?.contains("tweets") == true) {
                return "$instanceHost${uri.path?.replace("/tweets", "")}$search"
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
        override fun redirect(uri: UriKt, instanceHost: String): String {
            if (uri.host == "youtu.be" || (uri.host?.endsWith("youtube.com") == true && uri.path?.startsWith("/live") == true)) {
                val lastSlashIdx = uri.path?.lastIndexOf('/')
                if (lastSlashIdx != null) {
                    val watch = uri.path?.substring(lastSlashIdx + 1)
                    return "${instanceHost}/watch?v=${watch}"
                }
            }

            if (uri.host?.endsWith("youtube.com") == true && uri.path?.startsWith("/redirect?") == true) {
                return uri.toString()
            }

            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    Poketube("poketube") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.startsWith("/live_chat") == true) {
                return null
            }
            if (uri.path?.startsWith("/channel") == true) {
                val match = Regex("\\/channel\\/(.*)\\/?\$").find(uri.path!!)

                if (match != null) {
                    val id = match.groupValues[1]
                    return "${instanceHost}/channel?id=${id}?${uri.query}"
                }
            }

            if (uri.path?.let { Regex("\\/@[a-z]+\\/").containsMatchIn(it) } == true) {
                return instanceHost
            }

            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    Scribe("scribe") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val result = uri.host?.let { Regex("^(link|cdn-images-\\d+|.*)\\.medium\\.com").find(it)?.groupValues }
            if (result != null && result.size > 2) {
                val subdomain = result[1]
                if (subdomain != "link" || !subdomain.startsWith("cdn-images")) {
                    return "${instanceHost}/@${subdomain}${uri.path}?${uri.query}"
                }
            }

            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    SimplyTranslate("simplyTranslate") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
            return "$instanceHost/?${uri.query}"
        }
    },
    LibreTranslate("libreTranslate") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
            val query = uri.query
                ?.replace("sl", "source")
                ?.replace("tl", "target")
                ?.replace("text", "q")
            return "$instanceHost/?${query}"
        }

    },
    OSM("osm") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val travelModes = mapOf(
                "driving" to "fossgis_osrm_car",
                "walking" to "fossgis_osrm_foot",
                "bicycling" to "fossgis_osrm_bike",
                "transit" to "fossgis_osrm_car"
            )

            val mapCentreData = convertMapCentre(uri)
            var mapCentre = "#"
            if (mapCentreData != null) {
                mapCentre = "#map=${mapCentreData.first}/${mapCentreData.second}/${mapCentreData.third}"
            }

            val prefs = mutableMapOf<String, String>()
//            if(uri.splitQuery.containsKey("layer")){
//                prefs["layers"] =
//            }

            // we currently can't implement /dir and /embed since they send extra requests which we don't currently support
            if (uri.path != null && uri.path!!.contains("data=")) {
                dataLatLngRegex.find(uri.path!!)?.groupValues?.let {
                    val (_, mlat, mlon) = it
                    return "$instanceHost/search?query=$mlat%2C$mlon"
                }
            } else if (uri.splitQuery.containsKey("ll")) {
                val (mlat, mlon) = uri.splitQuery["ll"]!!.split(",")
                return "$instanceHost/search?query=$mlat%2C$mlon"
            } else if (uri.splitQuery.containsKey("viewpoint")) {
                val (mlat, mlon) = uri.splitQuery["viewpoint"]!!.split(",")
                return "$instanceHost/search?query=$mlat%2C$mlon"
            } else {
                val query = if (uri.splitQuery.containsKey("q")) {
                    uri.splitQuery["q"]!!
                } else if (uri.splitQuery.containsKey("query")) {
                    uri.splitQuery["query"]!!
                } else if (uri.path != null) {
                    placeRegex.find(uri.path!!)?.groupValues?.let { it[1] }
                } else null

                if (query != null) {
                    return "$instanceHost/search?query=${query}${mapCentre}&${
                        URLEncoder.encode(
                            prefs.makeQuery(),
                            StandardCharsets.UTF_8
                        )
                    }"
                }
            }

            return "$instanceHost/${mapCentre}&${
                URLEncoder.encode(
                    prefs.makeQuery(),
                    StandardCharsets.UTF_8
                )
            }"
        }
    },
    Facil("facil") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val travelModes = mapOf(
                "driving" to "car",
                "walking" to "pedestrian",
                "bicycling" to "bicycle",
                "transit" to "car"
            )

            val mapCentreData = convertMapCentre(uri)
            var mapCentre = "#"
            if (mapCentreData != null) {
                mapCentre = "#${mapCentreData.first}/${mapCentreData.second}/${mapCentreData.third}"
            }

            val prefs = mutableMapOf<String, String>()
//            if(uri.splitQuery.containsKey("layer")){
//                prefs["layers"] =
//            }

            if (uri.path != null && uri.path!!.contains("/embed")) {
                val query = if (uri.splitQuery.containsKey("q")) {
                    uri.splitQuery["q"]!!
                } else if (uri.splitQuery.containsKey("query")) {
                    uri.splitQuery["query"]!!
                } else if (uri.splitQuery.containsKey("pb")) {
                    kotlin.runCatching { uri.splitQuery["pb"]!!.split(Regex("!2s(.*?)!"))[1] }.getOrNull()
                } else null

                return "$instanceHost/#q=$query"
            } else if (uri.path != null && uri.path!!.contains("/dir")) {
                val travelMode = uri.splitQuery["travelmode"]
                val origin = uri.splitQuery["origin"]
                val destination = uri.splitQuery["destination"]

                return instanceHost + "/#q=" + origin + "%20to%20" + destination + "%20by%20" + travelModes[travelMode]
            } else if (uri.path != null && uri.path!!.contains("data=")) {
                dataLatLngRegex.find(uri.path!!)?.groupValues?.let {
                    val (_, mlat, mlon) = it
                    return "$instanceHost/#q=${mlat}%2C${mlon}"
                }
            } else if (uri.splitQuery.containsKey("ll")) {
                val (mlat, mlon) = uri.splitQuery["ll"]!!.split(",")
                return "$instanceHost/#q=${mlat}%2C${mlon}"
            } else if (uri.splitQuery.containsKey("viewpoint")) {
                val (mlat, mlon) = uri.splitQuery["viewpoint"]!!.split(",")
                return "$instanceHost/#q=${mlat}%2C${mlon}"
            } else {
                val query = if (uri.splitQuery.containsKey("q")) {
                    uri.splitQuery["q"]!!
                } else if (uri.splitQuery.containsKey("query")) {
                    uri.splitQuery["query"]!!
                } else if (uri.path != null) {
                    placeRegex.find(uri.path!!)?.groupValues?.let { it[1] } ?: ""
                } else ""

                return "${instanceHost}/$mapCentre/Mpnk/$query"
            }

            return null
        }
    },
    LingVa("lingva") {
        override fun redirect(uri: UriKt, instanceHost: String): String {
            val params = uri.splitQuery
            if (params.containsKey("sl") && params.containsKey("tl") && params.containsKey("text"))
                return "${instanceHost}/${params["sl"]}/${params["tl"]}/${params["text"]}"

            return instanceHost
        }
    },
    BreezeWiki("breezeWiki") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            return null
        }
    },
    Rimgo("rimgo") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (Regex("^https?:\\/{2}(?:[im]\\.)?stack\\.").containsMatchIn(uri.toString())) {
                return "$instanceHost/stack${uri.path}?${uri.query}"
            }

            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    LibReddit("libreddit") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val result = uri.host?.let { Regex("^(?:(?:external-)?preview|i)(?=\\.redd\\.it)").find(it) }
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
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.host?.let { Regex("^(?:(?:external-)?preview|i)\\.redd\\.it").containsMatchIn(it) } == true) {
                if (uri.query == null) {
                    return "$instanceHost${uri.path}?teddit_proxy=${uri.host}"
                }

                return "$instanceHost${uri.path}?${uri.query}&teddit_proxy=${uri.host}"
            }

            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    Neuters("neuters") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.startsWith("/article/") == true || uri.path?.startsWith("/pf/") == true || uri.path?.startsWith(
                    "/arc/"
                ) == true || uri.path?.startsWith(
                    "/resizer/"
                ) == true
            ) {
                return null
            }

            return "${instanceHost}${uri.path}"
        }
    },
    Dumb("dumb") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.endsWith("-lyrics") == true) {
                return "${instanceHost}${uri.path}"
            }

            return null
        }
    },
    RuralDictionary("ruralDictionary") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.contains("/define.php") == false && uri.path?.contains("/random.php") == false && uri.path != "/") return null
            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    AnonymousOverflow("anonymousOverflow") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.startsWith("/questions") == false && uri.path != "/") return null
            val threadID = Regex("\\/(\\d+)\\/?\$").find(uri.path!!)?.groupValues
            if (threadID != null) return "${instanceHost}/questions/${threadID[1]}?${uri.query}"
            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    BiblioReads("biblioReads") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.startsWith("/book/show/") == false && uri.path != "/") return null
            return "${instanceHost}${uri.path}?${uri.query}"
        }
    },
    WikiLess("wikiless") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val queries = uri.splitQuery

            val link = "$instanceHost${uri.path}"
            val urlSplit = uri.host?.split(".") ?: listOf()
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
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.path?.startsWith("/email") == true) return null
            return "$instanceHost${uri.path}?${uri.query}"
        }
    },
    WayBackClassic("waybackClassic") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val result = uri.path?.let { Regex("^\\/\\web\\/[0-9]+\\*\\/(.*)").find(it)?.groupValues }
            if (result != null) {
                val link = result[1]
                return "${instanceHost}/cgi-bin/history.cgi?utf8=✓&q=${URLEncoder.encode(link, StandardCharsets.UTF_8)}"
            }

            return instanceHost
        }
    },
    Gothub("gothub") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            val regex = uri.path?.let { Regex("^\\/(.*)\\/(.*)\\/(?:blob|tree)\\/(.*)\\/(.*)").find(it)?.groupValues }
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
    MikuInvidious("mikuInvidious") {
        override fun redirect(uri: UriKt, instanceHost: String): String? {
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
        override fun redirect(uri: UriKt, instanceHost: String): String? {
            if (uri.host == "bandcamp.com" && uri.path == "/search") {
                val query = uri.splitQuery["q"]
                return "${instanceHost}/search.php?query=${URLEncoder.encode(query, StandardCharsets.UTF_8)}"
            }
            if (uri.host?.endsWith("bandcamp.com") == true) {
                val regex = uri.host?.let { Regex("^(.*)\\.bandcamp\\.com").find(it)?.groupValues } ?: return null
                val artist = regex[1]
                if (uri.path == "/") {
                    return "${instanceHost}/artist.php?name=${artist}"
                } else {
                    val match = uri.path?.let { Regex("^\\/(.*)\\/(.*)").find(it)?.groupValues }
                    if (match != null) {
                        val type = match[1]
                        val name = match[2]
                        return "${instanceHost}/release.php?artist=${artist}&type=${type}&name=${name}"
                    }
                }
            }
            if (uri.host == "f4.bcbits.com") {
                val regex = uri.path?.let { Regex("\\/img\\/(.*)").find(it)?.groupValues } ?: return null
                val image = regex[1]
                return "${instanceHost}/image.php?file=${image}"
            }
            if (uri.host == "t4.bcbits.com") {
                val regex = uri.path?.let { Regex("\\/stream\\/(.*)\\/(.*)\\/(.*)").find(it)?.groupValues }
                if (regex != null) {
                    val directory = regex[1]
                    val format = regex[2]
                    val file = regex[3]
                    val token = uri.splitQuery["token"]
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

    abstract fun redirect(uri: UriKt, instanceHost: String): String?

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
        "mikuInvidious" to listOf("https://mikuinv.resrv.org"),
        "tent" to listOf("https://tent.sny.sh"),
        "wolfreeAlpha" to listOf("https://gqq.gitlab.io", "https://uqq.gitlab.io"),
        "libreSpeed" to listOf("https://librespeed.org"),
        "jitsi" to listOf("https://meet.jit.si", "https://8x8.vc"),
    )

    fun redirect(url: String, frontendKey: String, instance: String): String? {
        return RedirectFrontend.findFrontendByKey(frontendKey)?.redirect(UriKt(url), instance)
    }

    fun findServiceForUrl(url: String, services: List<LibRedirectService>): LibRedirectService? {
        return services.find { service ->
            service.targets.any { target -> target.containsMatchIn(url) }
        }
    }

    fun getDefaultInstanceForFrontend(frontendKey: String, instances: List<LibRedirectInstance>): String? {
        return instances.find { it.frontendKey == frontendKey }?.hosts?.firstOrNull()
    }
}

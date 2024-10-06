package fe.libredirectkt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MainTest {
    @Test
    fun testInstagram() {
        val services = LibRedirectLoader.loadBuiltInServices()
        val instances = LibRedirectLoader.loadBuiltInInstances()

        val instagramService = LibRedirect.findServiceForUrl("https://www.instagram.com/p/DAtQDJFPtfi", services)
        assertNotNull(instagramService)
        assertNotNull(instagramService.defaultFrontend)
        assertEquals("instagram", instagramService.key)
        assertEquals("proxigram", instagramService.defaultFrontend.key)
        assertEquals(
            "https://ig.opnxng.com",
            LibRedirect.getDefaultInstanceForFrontend(instagramService.defaultFrontend.key, instances)
        )

        assertEquals(
            "https://ig.opnxng.com/p/DAtQDJFPtfi", LibRedirect.redirect(
                "https://www.instagram.com/p/DAtQDJFPtfi",
                instagramService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(instagramService.defaultFrontend.key, instances)!!
            )
        )
    }

    @Test
    fun testYoutube() {
        val services = LibRedirectLoader.loadBuiltInServices()
        val instances = LibRedirectLoader.loadBuiltInInstances()

        val ytService = LibRedirect.findServiceForUrl("https://www.youtube.com/watch?v=V3zLnSGVdmE", services)
        assertNotNull(ytService)
        assertNotNull(ytService.defaultFrontend)
        assertEquals("youtube", ytService.key)
        assertEquals("invidious", ytService.defaultFrontend.key)
        assertEquals(
            "https://inv.nadeko.net",
            LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key, instances)
        )

        assertEquals(
            "https://inv.nadeko.net/watch?v=V3zLnSGVdmE", LibRedirect.redirect(
                "https://www.youtube.com/watch?v=V3zLnSGVdmE",
                ytService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key, instances)!!
            )
        )

        assertEquals(
            "https://inv.nadeko.net/watch?v=V3zLnSGVdmE", LibRedirect.redirect(
                "https://youtu.be/V3zLnSGVdmE",
                ytService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key, instances)!!
            )
        )
    }

    @Test
    fun testTwitter() {
        val services = LibRedirectLoader.loadBuiltInServices()
        val instances = LibRedirectLoader.loadBuiltInInstances()
        val twitterService =
            LibRedirect.findServiceForUrl("https://twitter.com/MishaalRahman/status/1633529635253174274", services)
        assertNotNull(twitterService)
        assertNotNull(twitterService.defaultFrontend)
        assertEquals("twitter", twitterService.key)
        assertEquals("nitter", twitterService.defaultFrontend.key)
        assertEquals(
            "https://xcancel.com",
            LibRedirect.getDefaultInstanceForFrontend(twitterService.defaultFrontend.key, instances)
        )
        assertEquals(
            "https://xcancel.com/MishaalRahman/status/1633529635253174274#m", LibRedirect.redirect(
                "https://twitter.com/MishaalRahman/status/1633529635253174274",
                twitterService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(twitterService.defaultFrontend.key, instances)!!
            )
        )
    }

    @Test
    fun testOSM() {
        val services = LibRedirectLoader.loadBuiltInServices()
        val instances = LibRedirectLoader.loadBuiltInInstances()
        val googleMapsUrl =
            "https://www.google.com/maps/place/41%C2%B001'58.2%22N+40%C2%B029'18.2%22E/@41.032833,40.4862063,17z/data=!3m1!4b1!4m6!3m5!1s0x0:0xf64286eaf72fc49d!7e2!8m2!3d41.0328329!4d40.4883948"
        val osmService = LibRedirect.findServiceForUrl(googleMapsUrl, services)
        assertNotNull(osmService)
        assertNotNull(osmService.defaultFrontend)
        assertEquals("maps", osmService.key)
        val osmFrontend = osmService.frontends.find { it.key == "osm" }
        assertNotNull(osmFrontend?.key)
        assertEquals("osm", osmFrontend!!.key)
        val osmInstance = instances.find { it.frontendKey == "osm" }
        assertNotNull(osmInstance)
        val osmHost = osmInstance.hosts.find { it == "https://www.openstreetmap.org" }
        assertNotNull(osmHost)
        assertEquals(
            "https://www.openstreetmap.org/search?query=41.0328329%2C40.4883948",
            LibRedirect.redirect(googleMapsUrl, osmFrontend.key, osmHost)
        )

        assertEquals(
            "https://www.openstreetmap.org/search?query=38.882147%2C-76.99017",
            LibRedirect.redirect("https://maps.google.com/?ll=38.882147,-76.99017", osmFrontend.key, osmHost)
        )

        assertEquals(
            "https://www.openstreetmap.org/search?query=48.857832%2C2.295226",
            LibRedirect.redirect(
                "https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=48.857832,2.295226&heading=-45&pitch=38&fov=80",
                osmFrontend.key,
                osmHost
            )
        )

        assertEquals(
            "https://www.openstreetmap.org/#map=14/48.1954385/16.3437521&",
            LibRedirect.redirect("https://www.google.com/maps/@48.1954385,16.3437521,14z", osmFrontend.key, osmHost)
        )
    }

    @Test
    fun testFacil() {
        val services = LibRedirectLoader.loadBuiltInServices()
        val instances = LibRedirectLoader.loadBuiltInInstances()
        val googleMapsUrl =
            "https://www.google.com/maps/place/41%C2%B001'58.2%22N+40%C2%B029'18.2%22E/@41.032833,40.4862063,17z/data=!3m1!4b1!4m6!3m5!1s0x0:0xf64286eaf72fc49d!7e2!8m2!3d41.0328329!4d40.4883948"
        val facilService = LibRedirect.findServiceForUrl(googleMapsUrl, services)
        assertNotNull(facilService)
        assertNotNull(facilService.defaultFrontend)
        assertEquals("maps", facilService.key)
        assertEquals("osm", facilService.defaultFrontend.key)
        // TODO: No facil instances exist in default config for some reason, fall back to hardcoded one (which is still available and working?)
        val facilInstance = instances.find { it.frontendKey == "facil" } ?: LibRedirectInstance("facil", listOf("https://facilmap.org"))
        assertNotNull(facilInstance)
        val facilHost = facilInstance.hosts.find { it == "https://facilmap.org" }
        assertNotNull(facilHost)
        assertEquals(
            "https://facilmap.org/search?query=41.0328329%2C40.4883948",
            LibRedirect.redirect(googleMapsUrl, facilService.defaultFrontend.key, facilHost)
        )

        assertEquals(
            "https://facilmap.org/search?query=38.882147%2C-76.99017",
            LibRedirect.redirect(
                "https://maps.google.com/?ll=38.882147,-76.99017",
                facilService.defaultFrontend.key,
                facilHost
            )
        )

        assertEquals(
            "https://facilmap.org/search?query=48.857832%2C2.295226",
            LibRedirect.redirect(
                "https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=48.857832,2.295226&heading=-45&pitch=38&fov=80",
                facilService.defaultFrontend.key, facilHost
            )
        )

        assertEquals(
            "https://facilmap.org/#map=14/48.1954385/16.3437521&",
            LibRedirect.redirect(
                "https://www.google.com/maps/@48.1954385,16.3437521,14z",
                facilService.defaultFrontend.key,
                facilHost
            )
        )
    }
}

package fe.libredirectkt

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MainTest {
    @Test
    fun testLibRedirect() {
        val services = LibRedirectLoader.loadBuiltInServices()

        val ytService = LibRedirect.findServiceForUrl("https://www.youtube.com/watch?v=V3zLnSGVdmE", services)
        assertNotNull(ytService)
        assertNotNull(ytService.defaultFrontend)
        assertEquals("youtube", ytService.key)
        assertEquals("invidious", ytService.defaultFrontend.key)
        assertEquals(
            "https://inv.vern.cc",
            LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key)?.firstOrNull()
        )

        assertEquals(
            "https://inv.vern.cc/watch?v=V3zLnSGVdmE", LibRedirect.redirect(
                "https://www.youtube.com/watch?v=V3zLnSGVdmE",
                ytService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key)?.first()!!
            )
        )
    }
}

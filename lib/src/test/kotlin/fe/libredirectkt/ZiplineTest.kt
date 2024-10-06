package fe.libredirectkt

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ZiplineTest {
    private fun newDispatcher(): ExecutorCoroutineDispatcher {
        return Executors.newSingleThreadExecutor { Thread(it, "Zipline") }.asCoroutineDispatcher()
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

        val dispatcher = newDispatcher()
        val libRedirectZipline = LibRedirectNew.create(dispatcher, LibRedirectResource.getLibRedirect())

        assertEquals(
            "https://inv.nadeko.net/watch?v=V3zLnSGVdmE", libRedirectZipline.redirect(
                "https://www.youtube.com/watch?v=V3zLnSGVdmE",
                ytService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key, instances)!!
            )
        )

        assertEquals(
            "https://inv.nadeko.net/watch?v=V3zLnSGVdmE", libRedirectZipline.redirect(
                "https://youtu.be/V3zLnSGVdmE",
                ytService.defaultFrontend.key,
                LibRedirect.getDefaultInstanceForFrontend(ytService.defaultFrontend.key, instances)!!
            )
        )

        libRedirectZipline.close()
    }
}

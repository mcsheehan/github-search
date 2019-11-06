package net.marksheehan.githubsearch

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        //TODO add tests using mockito
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("net.marksheehan.githubsearch", appContext.packageName)
    }
}

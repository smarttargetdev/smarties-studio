package br.com.smarttarget.ai.studio.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AuthManagerTest {

    @Test
    fun testGoogleChooserUrlWithEmail() {
        val email = "developer@gmail.com"
        val url = AuthManager.buildGoogleChooserUrl(email)
        assertTrue(url.contains("AccountChooser"))
        assertTrue(url.contains(email))
        assertTrue(url.contains("aistudio.google.com"))
    }

    @Test
    fun testGoogleChooserUrlWithoutEmail() {
        val url = AuthManager.buildGoogleChooserUrl(null)
        assertEquals(AuthManager.GOOGLE_SIGNIN_URL, url)
        assertTrue(url.contains("ServiceLogin"))
        assertTrue(url.contains("aistudio.google.com"))
    }
}

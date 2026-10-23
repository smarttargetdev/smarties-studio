package com.example.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SecurityManagerTest {

    @Test
    fun testAllowedDomains() {
        assertTrue(SecurityManager.isAllowedDomain("https://aistudio.google.com/prompts/new_chat"))
        assertTrue(SecurityManager.isAllowedDomain("https://makersuite.google.com"))
        assertTrue(SecurityManager.isAllowedDomain("https://accounts.google.com/signin/v2"))
        assertTrue(SecurityManager.isAllowedDomain("https://ai.google.dev/docs"))
        assertTrue(SecurityManager.isAllowedDomain("https://developers.google.com"))
        assertTrue(SecurityManager.isAllowedDomain("https://myaccount.google.com"))
    }

    @Test
    fun testBlockedUntrustedDomains() {
        assertFalse(SecurityManager.isAllowedDomain("https://evil-phishing-aistudio.com"))
        assertFalse(SecurityManager.isAllowedDomain("http://aistudio.google.com.attacker.com"))
        assertFalse(SecurityManager.isAllowedDomain("https://random-website.org"))
        assertFalse(SecurityManager.isAllowedDomain(""))
        assertFalse(SecurityManager.isAllowedDomain(null))
    }

    @Test
    fun testHttpsValidation() {
        assertTrue(SecurityManager.isSecureHttps("https://aistudio.google.com"))
        assertFalse(SecurityManager.isSecureHttps("http://aistudio.google.com"))
        assertFalse(SecurityManager.isSecureHttps("ftp://aistudio.google.com"))
    }

    @Test
    fun testHostExtraction() {
        assertEquals("aistudio.google.com", SecurityManager.extractDisplayHost("https://aistudio.google.com/library"))
        assertEquals("accounts.google.com", SecurityManager.extractDisplayHost("https://accounts.google.com/ServiceLogin"))
    }

    @Test
    fun testEvaluateSecurity() {
        val state = SecurityManager.evaluateSecurity("https://aistudio.google.com/prompts")
        assertTrue(state.isHttps)
        assertTrue(state.isAllowedDomain)
        assertTrue(state.isSafeBrowsingActive)
        assertEquals("aistudio.google.com", state.host)
    }
}

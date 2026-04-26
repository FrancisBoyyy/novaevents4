package pt.unl.fct.iadi.novaevents

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LayoutTemplateTest {

    @Test
    fun `layout template includes navbar username class`() {
        val resource = requireNotNull(javaClass.classLoader.getResource("templates/layout.html")) {
            "Could not find templates/layout.html on the test classpath"
        }

        val html = resource.readText()

        assertTrue(
            html.contains("navbar-username"),
            "layout.html should include a class named navbar-username on the logged-in username element"
        )
    }
}

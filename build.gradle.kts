plugins {
    id("com.gitlab.grrfe.common-gradle-plugin")
}

library {
    group.set("fe.libredirectkt")
    jvm.set(17)
}

dependencies {
    relocate("com.gitlab.grrfe:gson-ext:11.0.0")
    relocate("com.google.code.gson:gson:2.10.1")
    relocate("com.github.1fexd:uriparser:0.0.7")
}

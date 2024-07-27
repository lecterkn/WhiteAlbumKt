package me.lecter.whitealbum.client.enum

enum class APILanguage(val value: String) {
    AE("ar-AE"),
    DE("de-DE"),
    EN("en-US"),
    ES("es-ES"),
    MX("es-MX"),
    FR("fr-FR"),
    ID("id-ID"),
    IT("it-IT"),
    JP("ja-JP"),
    KR("ko-KR"),
    PL("pl-PL"),
    BR("pt-BR"),
    RU("ru-RU"),
    TH("th-TH"),
    TR("tr-TR"),
    VN("vi-VN"),
    CN("zh-CN"),
    TW("zh-TW"),
    ;

    companion object {
        fun getLanguage(language: String): APILanguage? {
            for (lang in APILanguage.entries) {
                if (lang.value == language) {
                    return lang
                }
            }
            return null
        }
    }
}

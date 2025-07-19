package com.mignon.springsecurity.util

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class CustomBannerPrinter : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        printCustomBanner()
    }

    private fun printCustomBanner() {
        val banner = """
                                                    
 /'\_/`\  __                                    ™  
/\      \/\_\     __     ___     ___     ___    
\ \ \__\ \/\ \  /'_ `\ /' _ `\  / __`\ /' _ `\  
 \ \ \_/\ \ \ \/\ \L\ \/\ \/\ \/\ \L\ \/\ \/\ \ 
  \ \_\\ \_\ \_\ \____ \ \_\ \_\ \____/\ \_\ \_\
   \/_/ \/_/\/_/\/___L\ \/_/\/_/\/___/  \/_/\/_/
                  /\____/                       
                  \_/__/                        
                    
             Version: 1.0.0-SNAPSHOT
      doc adress : http://127.0.0.1/doc.html
          adress : http://127.0.0.1
           """.trimIndent()

        println("\n")
        println(banner)
        println("\n")
    }
}
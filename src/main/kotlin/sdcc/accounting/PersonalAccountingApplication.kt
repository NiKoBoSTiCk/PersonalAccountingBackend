package sdcc.accounting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonalAccountingApplication

fun main(args: Array<String>) {
    runApplication<PersonalAccountingApplication>(*args)
}

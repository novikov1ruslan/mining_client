import org.apache.commons.cli.*

class ArgumentParser {
    val options = Options()

    @Throws(ParseException::class)
    fun parse(args: Array<String>): Arguments {
        val serverOption = Option("s", "server", true, "remote server")
        serverOption.isRequired = false
        options.addOption(serverOption)

        val nameOption = Option("n", "name", true, "name for this computer")
        nameOption.isRequired = true
        options.addOption(nameOption)

        val emailOption = Option("e", "email", true, "user's email")
        emailOption.isRequired = true
        options.addOption(emailOption)

        val parser = DefaultParser()
        val parse = parser.parse(options, args)

        val server = when {
            parse.hasOption('s') -> parse.getOptionValue('s')
            else -> "http://localhost:8080"
        }

        return Arguments(parse.getOptionValue('e'), parse.getOptionValue('n'), server)
    }
}

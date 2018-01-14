import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.ParseException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


fun main(args: Array<String>) {
    println("Connector Client (v3.1)")
    val localMachine = java.net.InetAddress.getLocalHost()
    println("Hostname of local machine: ${localMachine.hostName}\n")

    val parser = ArgumentParser()

    val arguments: Arguments
    try {
        arguments = parser.parse(args)
    } catch (e: ParseException) {
        println(e.message)
        val formatter = HelpFormatter()
        formatter.printHelp("miner_client", parser.options)
        return
    }

    println("\n-----------------------------")
    println("User: [${arguments.email}]")
    println("Miner: [${arguments.name}]")
    println("Server address: [${arguments.server}]")

    val url = URL(arguments.server + "/ping")
    val pingPeriod = 2 * 60 * 1000L
    startPingLoop(url, pingPeriod, email = arguments.email, id = arguments.name)
}

private fun startPingLoop(url: URL, pingPeriod: Long, email: String, id: String) {
    //language=JSON
    val body = "{\n  \"id\":\"$id\",\n  \"email\":\"$email\"\n}"

    while (true) {
        val message = StringBuffer(Date().toString())
        try {
            message.append(" pinging $url ...")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            with(httpURLConnection) {
                requestMethod = "POST"
                doOutput = true
                setRequestProperty("Content-Type", "application/json")

                val output = OutputStreamWriter(outputStream)
                output.write(body)
                output.flush()
                output.close()

                val input = BufferedReader(InputStreamReader(inputStream))
                message.append(" " + input.readLine())
                input.close()
            }
        } catch (e: Exception) {
            message.append(" " + e)
        }

        println(message)
        Thread.sleep(pingPeriod)
    }
}


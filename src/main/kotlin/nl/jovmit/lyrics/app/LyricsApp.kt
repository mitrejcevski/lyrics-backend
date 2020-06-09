package nl.jovmit.lyrics.app

import org.eclipse.jetty.http.HttpStatus.NOT_IMPLEMENTED_501
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Filter
import spark.Spark
import spark.Spark.*

class LyricsApp {

    private companion object {
        private const val API_NOT_IMPLEMENTED_ERROR = "API not implemented."
        private const val INTERNAL_SERVER_ERROR = "Internal server error."
    }

    private val logger: Logger = LoggerFactory.getLogger(LyricsApp::class.java)
    private val routes = Routes()

    fun start() {
        port(8080)
        enableCORS()
        setLog()
        routes.create()
        configureInternalServerError()
        configureNotImplementedError()
    }

    fun stop() {
        Spark.stop()
    }

    fun awaitInitialization() {
        Spark.awaitInitialization()
    }

    private fun enableCORS() {
        before(Filter { _, response ->
            response.header("Access-Control-Allow-Origin", "*")
            response.header("Access-Control-Allow-Headers", "*")
            response.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS")
        })
    }

    private fun setLog() {
        before(Filter { request, _ ->
            logger.info(
                "URL Request: ${request.requestMethod()} ${request.uri()} - headers: ${request.headers()}"
            )
        })
    }

    private fun configureInternalServerError() {
        internalServerError { request, response ->
            response.status(NOT_IMPLEMENTED_501)
            logger.error(INTERNAL_SERVER_ERROR + ": " + request.pathInfo())
            INTERNAL_SERVER_ERROR
        }
    }

    private fun configureNotImplementedError() {
        notFound { request, response ->
            response.status(NOT_IMPLEMENTED_501)
            logger.error(API_NOT_IMPLEMENTED_ERROR + ": " + request.pathInfo())
            API_NOT_IMPLEMENTED_ERROR
        }
    }
}
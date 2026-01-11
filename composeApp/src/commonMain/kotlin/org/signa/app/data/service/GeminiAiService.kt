package org.signa.app.data.service

import io.ktor.client.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import org.signa.app.domain.model.Signal
import org.signa.app.domain.core.Result
import org.signa.app.domain.core.DataError

class GeminiAiService(
    private val apiKey: String,
    private val modelName: String = "gemini-2.5-flash"
) : AiService {

    private val client = HttpClient {
        install(HttpTimeout){
            requestTimeoutMillis = 60000
            connectTimeoutMillis = 60000
            socketTimeoutMillis = 60000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                }
            }
            level = LogLevel.ALL
        }
    }

    override suspend fun analyzeSignals(signals: List<Signal>): Result<String, DataError> {
        if (apiKey.isBlank()) {
            return Result.Error(DataError.NO_INTERNET)
        }

        return try {
            val signalDescription = signals.joinToString("\n") { signal ->
                """
                - org.signa.app.domain.model.Signal: ${signal.name} (${signal.type})
                  Strength: ${signal.strength} dBm
                  Frequency: ${signal.frequency ?: "Unknown"}
                  Suspicious: ${signal.isSuspicious}
                """.trimIndent()
            }

            val prompt = """
                Analyze the following radio signals detected in the environment. 
                Identify potential threats, security risks, or anomalies.
                
                Signals:
                $signalDescription
                
                Provide a concise security report.
            """.trimIndent()

            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"


            val response: HttpResponse = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(
                    buildJsonObject {
                        putJsonArray("contents") {
                            addJsonObject {
                                putJsonArray("parts") {
                                    addJsonObject { put("text", prompt) }
                                }
                            }
                        }
                    }
                )
            }

            val responseText = response.bodyAsText()

            if (response.status.isSuccess()) {
                val jsonResponse = Json.parseToJsonElement(responseText).jsonObject
                val text = jsonResponse["candidates"]?.jsonArray?.getOrNull(0)
                    ?.jsonObject?.get("content")
                    ?.jsonObject?.get("parts")
                    ?.jsonArray?.getOrNull(0)
                    ?.jsonObject?.get("text")?.jsonPrimitive?.content ?: "Analiz cavabı formatlana bilmədi."

                Result.Success(text)
            } else {
                Result.Error(DataError.SERVER_ERROR)
            }
        } catch (e: Exception) {
            println("AI_EXCEPTION: ${e.message}")
            e.printStackTrace()
            Result.Error(DataError.UNKNOWN)
        }
    }
}
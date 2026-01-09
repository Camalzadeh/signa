package org.signa.app.data.service

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.* // Əlavə edildi
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import org.signa.app.domain.model.Signal
import org.signa.app.domain.service.AiService
import org.signa.app.domain.util.Result
import org.signa.app.domain.util.DataError

class GeminiAiService(
    private val apiKey: String,
    private val modelName: String = "gemini-2.5-flash"
) : AiService {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        // LOGLARI AKTIVLƏŞDIRIRIK
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR_LOG: $message")
                }
            }
            level = LogLevel.ALL // Bütün detalları (URL, Headers, Body) göstər
        }
    }

    override suspend fun analyzeSignals(signals: List<Signal>): Result<String, DataError> {
        if (apiKey.isBlank()) {
            println("AI_ERROR: API Key boşdur!")
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

            println("AI_INFO: Sorğu göndərilir: $url")

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
            println("AI_RESPONSE_STATUS: ${response.status}")

            if (response.status.isSuccess()) {
                val jsonResponse = Json.parseToJsonElement(responseText).jsonObject
                val text = jsonResponse["candidates"]?.jsonArray?.getOrNull(0)
                    ?.jsonObject?.get("content")
                    ?.jsonObject?.get("parts")
                    ?.jsonArray?.getOrNull(0)
                    ?.jsonObject?.get("text")?.jsonPrimitive?.content ?: "Analiz cavabı formatlana bilmədi."

                Result.Success(text)
            } else {
                println("AI_SERVER_ERROR: $responseText")
                Result.Error(DataError.SERVER_ERROR)
            }
        } catch (e: Exception) {
            println("AI_EXCEPTION: ${e.message}")
            e.printStackTrace() // Bütün xəta zəncirini görək
            Result.Error(DataError.UNKNOWN)
        }
    }
}
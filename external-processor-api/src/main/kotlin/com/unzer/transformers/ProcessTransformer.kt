package com.unzer.transformers

import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.ResponseTransformer
import com.github.tomakehurst.wiremock.http.HttpHeader.httpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.Response
import java.util.*

class ProcessTransformer : ResponseTransformer() {

    override fun getName() = "process-transformer"

    override fun applyGlobally() = false

    override fun transform(
        request: Request?,
        response: Response?,
        files: FileSource?,
        parameters: Parameters?
    ) = Response.Builder
        .like(response)
        .but()
        .headers(HttpHeaders(httpHeader("content-type", "application/json")))
        .body(successBody())
        .build()


    companion object {
        private fun uniqueId() = UUID.randomUUID().toString().replace("-", ".")
        private fun successBody() = "{\"approvalCode\":\"${uniqueId()}\"}"
    }
}
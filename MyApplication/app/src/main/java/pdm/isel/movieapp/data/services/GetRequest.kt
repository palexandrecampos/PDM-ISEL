package pdm.isel.movieapp.data.services

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.IOException

class GetRequest<DTO>(
        url: String,
        private var dtoType: Class<DTO>,
        private var success: (DTO) -> Unit,
        private var error: (VolleyError) -> Unit
) : JsonRequest<DTO>(Method.GET, url, "", success, error) {

    companion object {
        val mapper: ObjectMapper = jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<DTO> {

        try {
            val dto = mapper.readValue(response.data, dtoType)
            return Response.success(dto, null)
        } catch (e: IOException) {
            return Response.error(VolleyError())
        }
    }

    override fun deliverResponse(response: DTO) {
        success(response)
    }

    override fun deliverError(error: VolleyError?) {
        if (error != null) {
            error(error)
        }
    }
}
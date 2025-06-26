package com.example.tileloadingtest


import android.util.Log
import com.arcgismaps.arcgisservices.LevelOfDetail
import com.arcgismaps.arcgisservices.TileKey
import com.arcgismaps.geometry.Envelope
import com.arcgismaps.geometry.GeometryEngine
import com.arcgismaps.geometry.Point
import com.arcgismaps.geometry.SpatialReference
import com.arcgismaps.mapping.layers.CustomTiledLayer
import com.arcgismaps.mapping.layers.TileImageFormat
import com.arcgismaps.mapping.layers.TileInfo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes

object CustomTiledLayerFactory {
    private val ktorClient = HttpClient()

    private val wgs84Bounds = Envelope(
        -180.0000,
        -85.05113,
        180.0000,
        85.05113,
        spatialReference = SpatialReference.wgs84()
    )
    private val projectedBounds = GeometryEngine.projectOrNull(
        wgs84Bounds,
        SpatialReference(3857)
    ) as Envelope

    private val origin = Point(projectedBounds.xMin, projectedBounds.yMax, projectedBounds.spatialReference)

    private val levelsOfDetail = listOf(
        LevelOfDetail(2, scale = 139770566.007, resolution = 39135.76),
        LevelOfDetail(3, scale = 69885283.0036, resolution = 19567.88),
        LevelOfDetail(4, scale = 34942641.5018, resolution = 9783.94),
        LevelOfDetail(5, scale = 17471320.7509, resolution = 4891.97),
        LevelOfDetail(6, scale = 8735660.37545, resolution = 2445.985),
        LevelOfDetail(7, scale = 4367830.18773, resolution = 1222.9925),
        LevelOfDetail(8, scale = 2183915.09386, resolution = 611.49625),
        LevelOfDetail(9, scale = 1091957.54693, resolution = 305.748125),
        LevelOfDetail(10, scale = 545978.773466, resolution = 152.8740625),
        LevelOfDetail(11, scale = 272989.386733, resolution = 76.43703125),
        LevelOfDetail(12, scale = 136494.693366, resolution = 38.218515625),
        LevelOfDetail(13, scale = 68247.3466832, resolution = 19.109257812),
        LevelOfDetail(14, scale = 34123.6733416, resolution = 9.554628906),
        LevelOfDetail(15, scale = 17061.8366708, resolution = 4.777314453),
        LevelOfDetail(16, scale = 8530.91833540, resolution = 2.388657227)
    )

    private val tileInfo = TileInfo(
        dpi = 96,
        format = TileImageFormat.Png,
        levelsOfDetail = levelsOfDetail,
        origin = origin,
        spatialReference = SpatialReference(3857),
        tileHeight = 256,
        tileWidth = 256
    )

    fun createCustomTiledLayer(): CustomTiledLayer {
        return CustomTiledLayer(
            tileInfo = tileInfo,
            fullExtent = projectedBounds,
            dataProvider = {
                getTile(it)
            }
        )
    }

    private suspend fun getTile(tileKey: TileKey): ByteArray? {
        try {
            val url =
                "https://tile.openstreetmap.org/${tileKey.level}/${tileKey.column}/${tileKey.row}.png"

            val response = ktorClient.get(url)

            return response.readRawBytes()
        } catch (e: Exception) {
            Log.e("CustomTiledLayer", "Error fetching tile: ${e.message}")
            return null
        }
    }
}
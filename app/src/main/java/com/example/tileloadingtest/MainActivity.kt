package com.example.tileloadingtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arcgismaps.geometry.Envelope
import com.arcgismaps.geometry.SpatialReference
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.Basemap
import com.arcgismaps.mapping.Viewpoint
import com.arcgismaps.toolkit.geoviewcompose.MapView

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val map = remember { createMap() }

            MapView(
                modifier = Modifier
                    .fillMaxSize(),
                arcGISMap = map,
            )
        }
    }


    fun createMap(): ArcGISMap {
        val customTiledLayer = CustomTiledLayerFactory.createCustomTiledLayer()

        val arcGISMap = ArcGISMap()
        arcGISMap.setBasemap(Basemap(customTiledLayer))

        arcGISMap.initialViewpoint = Viewpoint(
            Envelope(
                639372.1680333742,
                5433084.390629345,
                1187317.5201129832,
                6441709.72390181,
                spatialReference = SpatialReference(3857)
            )
        )
        return arcGISMap
    }
}
package com.masum.metro.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.metro.data.model.Line
import com.masum.metro.data.model.Station
import com.masum.metro.data.model.StationWithLine
import com.masum.metro.util.fromHex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationCard( stationWithLine: StationWithLine,onClick:(Station)->Unit) {
    ElevatedCard(
        onClick = { onClick.invoke(stationWithLine.station) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column (
            modifier = Modifier
                .padding(5.dp)
                ){
            Text(
                text = stationWithLine.station.name_en,
                fontSize =16.sp
            )

            LazyRow(){
                items(stationWithLine.lines){line->
                    Text(
                        modifier =Modifier

                            .padding(3.dp)
                            .background(color=fromHex(line.color), shape = RoundedCornerShape(50))
                            .padding(start = 5.dp, end = 5.dp),
                        text = line.name_en,
                        color = Color.White,
                        fontSize =8.sp
                    )
                }
            }
        }
        
    }
}



@Preview
@Composable
fun StationCardPreview() {


}
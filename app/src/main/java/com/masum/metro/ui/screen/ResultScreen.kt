package com.masum.metro.ui.screen

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.masum.metro.data.model.ResultModel
import com.masum.metro.data.model.StepModel
import com.masum.metro.ui.viewmodel.ResultViewModel
import com.masum.metro.util.*

@Composable
fun ResultScreenRoute(
    onBackClick:()->Unit,
    viewModel: ResultViewModel = hiltViewModel(),
) {
    val route by viewModel.route.collectAsStateWithLifecycle()
    val ad = viewModel.ad.collectAsStateWithLifecycle(initialValue = 0).value
    
    val context = LocalView.current.context

    Log.i("123321", "ResultScreenRoute:ad response $ad")
    
   /* if(ad==1)*/loadInterstitial(context)
   /* else if(ad==2) loadFacebookInterstitial(context,onBackClick)*/

    ResultScreen(resultModel = route,onBackClick,ad)

}


var mInterstitialAd: InterstitialAd? = null






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(resultModel: ResultModel, onBackClick: () -> Unit,ad:Int) {
    Scaffold(

    ) {
        val context = LocalView.current.context
        BackHandler() {


            Log.i("123321", "ResultScreen: reesult is $ad")

            when (ad) {
                1,2 -> showInterstitial(context){

                    onBackClick.invoke()
                }
          /*     3-> showFacebookInterstitial(context){
                    onBackClick.invoke()

                }*/
                else -> {
                    onBackClick.invoke()
                }
            }
        }
      
        Column(modifier = Modifier
            .padding(it)) {
            Row() {
                RowText("Price($)",text =resultModel.price)
                RowText("Minute",text = resultModel.time)
                RowText("Station",text = resultModel.stations)
                RowText("Lines",text = resultModel.line)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(1.dp, Color.White)
                    .padding(10.dp)
            ){
                val lastItemIndex =resultModel.step.size-1
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    resultModel.step.forEachIndexed { index, stepModel ->
                        Row(verticalAlignment = Alignment.Bottom){
                        CircleWithLine(index==0,stepModel.color)

                            if(index==0){
                                Column() {
                                Text(text = " Start at ${stepModel.title}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(text = stepModel.time)
                            }
                            }
                            else if(index==lastItemIndex){
                                Text(text = " Arrive at ${stepModel.title}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            else{
                                Column() {

                                    Text(text = " Change at ${stepModel.title}", fontSize = 18.sp)
                                    Text(text = stepModel.time)

                                }
                                }
                            }
                        }
                        }
                    }

            Button(
                onClick = {
                    when (ad) {
                        1,2 -> {
                            showInterstitial(context) { onBackClick.invoke() }
                        }

                        else -> onBackClick.invoke()
                    }
                },
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .padding(8.dp),
            ) {
                Text(text = "Change Route")
            }
                }
            }

        }



@Composable
fun RowScope.RowText(key:String,text:String) {
        Column(modifier = Modifier.weight(0.25f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = key,
                textAlign = TextAlign.Center
            )
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )

        }
}
@Composable
fun RowScope.RowNumber(text:String) {
        Text(
            text = text,
            modifier = Modifier.weight(0.25f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
}

@Preview
@Composable
fun PreviewResultScreen() {
    ResultScreen(
        ResultModel("1.67","40","15","3", step = arrayListOf(
            StepModel(0,"Start At Jurong East"),
            StepModel(1,"Change At Yishun"),
            StepModel(0,"Start At Bishan"),
        )),{},0
    )
}

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Column(
        modifier = modifier,

    ) {
        for (step in 0..numberOfSteps) {
            Step(
                modifier = Modifier,
                isCompete = step < currentStep,
                isCurrent = step == currentStep
            )
        }
    }
}

@Composable
fun Step(modifier: Modifier = Modifier, isCompete: Boolean, isCurrent: Boolean) {
    val color = if (isCompete || isCurrent) Color.Red else Color.LightGray
    val innerCircleColor = if (isCompete) Color.Red else Color.LightGray

    Box(modifier = modifier) {

        //Line
        Divider(
            modifier = Modifier
                .height(50.dp)  //fill the max height
                .width(1.dp),
            color = color,
            thickness = 2.dp
        )

        //Circle
        androidx.compose.foundation.Canvas(modifier = Modifier
            .size(15.dp)
            .align(Alignment.CenterEnd)
            .border(
                shape = CircleShape,
                width = 2.dp,
                color = color
            ),
            onDraw = {
                drawCircle(color = innerCircleColor)
            }
        )
    }
}

@Composable
fun CircleWithLine(b:Boolean,color:String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(b.not()){
                Divider(
                    color = fromHex(color),
                    modifier = Modifier
                        .padding(5.dp)
                        .height(50.dp)
                        .width(3.dp)
                )
            }
            androidx.compose.foundation.Canvas(modifier = Modifier
                .size(15.dp)
                .border(
                    shape = CircleShape,
                    width = 2.dp,
                    color = Color.Black
                ),
                onDraw = {
                    drawCircle(Color.White)
                }
            )

        }
    
}

@Preview
@Composable
fun previewCircleWithLine() {
    CircleWithLine(true,"EF1C2A")
}

@Preview
@Composable
fun StepsProgressBarPreview() {
    val currentStep = remember { mutableStateOf(1) }
    StepsProgressBar(modifier = Modifier.fillMaxWidth(), numberOfSteps = 3, currentStep = currentStep.value)
}
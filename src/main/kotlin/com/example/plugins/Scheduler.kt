package com.example.plugins

import com.example.database.model.Resources
import com.example.database.services.ApplicationService
import com.example.database.services.RateService
import kotlinx.coroutines.delay
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


suspend fun configureScheduler() {
    while (true) {
        val applicationService = ApplicationService()
        applicationService.checkRequests()
        applicationService.checkApplications()
        delay(1000 * 60 * 60 * 24)
    }
}

suspend fun bestMentorsReport(){
    while (true) {
        try {

            val rateService = RateService()
            val bestMentors = rateService.getBestMentors()

            if (bestMentors is Resources.Success){
                val simpleDateFormat = SimpleDateFormat("dd-MMMM-yyyy")
                val date = simpleDateFormat.format(Date())
                val filepath = "./bestMentors$date.xlsx"
                val xlWb = XSSFWorkbook()
                //Instantiate Excel worksheet:
                val xlWs = xlWb.createSheet()

                //Row index specifies the row in the worksheet (starting at 0):
                var rowNumber = 0
                //Cell index specifies the column within the chosen row (starting at 0):
                var columnNumber = 0

                val infoRow =  xlWs.createRow(rowNumber)
                infoRow.createCell(columnNumber).setCellValue("Назва предмету")
                columnNumber++
                infoRow.createCell(columnNumber).setCellValue("Ментор")
                columnNumber++
                infoRow.createCell(columnNumber).setCellValue("Середня оцінка")
                for (bestMentor in rateService.getBestMentors().data?: emptyList()){
                    rowNumber++
                    columnNumber = 0
                    val currentRow = xlWs.createRow(rowNumber)
                    currentRow.createCell(columnNumber).setCellValue(bestMentor.subject.name)
                    columnNumber++
                    currentRow.createCell(columnNumber).setCellValue("${bestMentor.mentor.name} ${bestMentor.mentor.surname}")
                    columnNumber++
                    currentRow.createCell(columnNumber).setCellValue(bestMentor.mentor.rate.toString())
                }

                //Write file:
                val outputStream = FileOutputStream(filepath)
                xlWb.write(outputStream)
            }



        }catch (e: Exception){
            e.printStackTrace()
        }
        delay(1000*60*60*24*30L)
    }
}
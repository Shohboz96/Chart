package uz.max.anychart

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_show.*
import uz.max.anychart.adapters.UserAdapter
import uz.max.anychart.api.ApiClient
import uz.max.anychart.api.ChartApi
import uz.max.anychart.api.ResponseData
import uz.max.anychart.data.ProductData
import uz.max.anychart.data.UserData
import uz.max.anychart.data.ValueData
import uz.max.anychart.dialog.FloatDialog
import uz.max.anychart.dialog.ProductDialog
import uz.max.anychart.dialog.UserDialog
import uz.max.anychart.dialog.ValueDialog
import uz.max.anychart.room.AppDatabase
import uz.max.anychart.room.entities.*
import uz.max.anychart.utils.ResultData
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val entryUser = ArrayList<PieEntry>()
    private val productManufacture = ArrayList<PieEntry>()
    private val money: ArrayList<Entry> = ArrayList()
    private val workers: ArrayList<Entry> = ArrayList()
    private val workersList: ArrayList<ValueData> = ArrayList()
    private val tasks: ArrayList<Entry> = ArrayList()
    private val tasksList: ArrayList<ValueData> = ArrayList()
    private val balance: ArrayList<Entry> = ArrayList()
    private val balanceList: ArrayList<ValueData> = ArrayList()

    private val api = ApiClient.retrofit.create(ChartApi::class.java)
    private val executor = Executors.newSingleThreadExecutor()
    private val room = AppDatabase.getDatabase(this)

    private lateinit var proDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showProgress()

        getBalance()


        frame_line_chart.setOnClickListener {
            val balanceList = ArrayList<ValueData>()
            val workersList = ArrayList<ValueData>()
            val moneyList = ArrayList<ValuesAllData>()
            allBalance {

                it.onData {list->
                    proDialog.dismiss()
                    list.data?.let { it1 -> balanceList.addAll(it1) }
                    Log.d("AAA","balance "+balanceList.size.toString())
                }
            }
            allWorkers {
                it.onData {it->

                    it.data?.let { it -> workersList.addAll(it) }
                    Log.d("AAA","workers "+workersList.size.toString())
                }
                for (i in balanceList.indices) {
                    moneyList.add(ValuesAllData(i,balanceList[i].value.toFloat() / workersList[i].value.toFloat(),0))
                }
                Log.d("AAA","money "+moneyList.size.toString())
                runOnUiThread {
                    val dialog = FloatDialog(this, moneyList)
                    dialog.show()
                }
            }

        }
        line_balance.setOnClickListener {
          allBalance {
              showProgress()
              val ls = ArrayList<ValueData>()
              it.onData {
                  proDialog.dismiss()
                  for (i in it.data!!.indices){
                      ls.add(ValueData(it.data[i].value))
                  }
                  runOnUiThread {
                      val dialog = ValueDialog(this, ls,"Balance")
                      dialog.show()
                  }
              }
          }
        }
        line_tasks.setOnClickListener {
            allTasks {
                showProgress()
                val ls = ArrayList<ValueData>()
                it.onData {
                    proDialog.dismiss()
                    for (i in it.data!!.indices){
                        ls.add(ValueData(it.data[i].value))
                    }
                    runOnUiThread {
                        val dialog = ValueDialog(this, ls,"Tasks")
                        dialog.show()
                    }
                }
            }
        }
        line_workers.setOnClickListener {

            allWorkers {
                showProgress()
                val ls = ArrayList<ValueData>()
                it.onData {
                    proDialog.dismiss()
                    for (i in it.data!!.indices){
                        ls.add(ValueData(it.data[i].value))
                    }
                    runOnUiThread {
                        val dialog = ValueDialog(this, ls,"Workers")
                        dialog.show()
                    }
                }
            }
        }
        pie_age.setOnClickListener {
            allAges {
                showProgress()
                it.onData {
                    proDialog.dismiss()
                    val ls = ArrayList<UsersData>()
                    for (i in it.indices) {
                        ls.add(UsersData(i, it[i].age, it[i].lastName, it[i].firstName))
                    }
                    runOnUiThread {
                        val dialog = UserDialog(this, ls)
                        dialog.show()
                    }
                }
            }
        }
        pie_manufactures.setOnClickListener {
            allManufactures {
                showProgress()
                it.onData { it ->
                    proDialog.dismiss()
                    val ls = ArrayList<ProductsData>()
                    for (i in it.indices) {
                        ls.add(ProductsData(i, it[i].modelName, it[i].serial, it[i].manufacturer))
                    }
                    runOnUiThread {
                        val dialog = ProductDialog(this, ls)
                        dialog.show()
                    }
                }
            }
        }

    }

    private fun showProgress() {
        proDialog = ProgressDialog(this)
        proDialog.show()
        proDialog.setContentView(R.layout.progress_dialog)
        proDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    private fun lineGraph(entry: ArrayList<Entry>) {

        lineChart.setNoDataText("No Balance yet!")

        lineChart.isAutoScaleMinMaxEnabled = true
        lineChart.legend.isEnabled = true
        lineChart.animateX(1500)

        val vl = LineDataSet(entry, "Daily Money")
        vl.setDrawFilled(true)

        vl.setDrawValues(false)
        vl.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        vl.lineWidth = 5f
        vl.setDrawCircles(true)

        val paint = lineChart.renderer.paintRender
        val height = lineChart.height.toFloat()
        val width = lineChart.width.toFloat()

        val lindGrad = LinearGradient(
            0f,
            0f,
            width,
            height,
            Color.BLUE,
            Color.parseColor("#E45CF4"),
            Shader.TileMode.REPEAT
        )
        paint.shader = lindGrad
        vl.fillColor = R.color.gray
        vl.fillAlpha = R.color.red



        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.description.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM


        lineChart.setTouchEnabled(false)

        lineChart.data = LineData(vl)
        lineChart.invalidate()
        lineChart.resetZoom()


        val markerView = CustomMarker(this, R.layout.marker_view)
        lineChart.marker = markerView

    }

    private fun getManufactures() {
        manufactures {
            it.onData { it ->
                executor.execute {
                    room.productDao().clearDB()
                    for (i in it.indices) {
                        val d = it[i]
                        room.productDao()
                            .insert(ProductsData(i, d.modelName, d.serial, d.manufacturer))
                    }
                }
                for (i in it.groupingBy { it.manufacturer }.eachCount()) {
                    productManufacture.add(PieEntry(i.value.toFloat(), i.key))
                }
                pieChart(pie_manufactures, productManufacture, "")
            }
        }
    }

    private fun pieChart(view: PieChart, pieEntry: ArrayList<PieEntry>, label: String) {

        val pieDataSet = PieDataSet(pieEntry, label)
        val pieData = PieData(pieDataSet)
        view.data = pieData
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()
        pieDataSet.sliceSpace = 0f
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 0f

        view.setTouchEnabled(false)
        view.setUsePercentValues(false)
        view.setEntryLabelTextSize(10f)

        view.description.isEnabled = false
        view.setNoDataText("No Balance yet!")
        view.invalidate()

        view.animateX(1800, Easing.EaseInBack)


        val markerView = CustomMarker(this, R.layout.marker_view)
        view.marker = markerView
    }

    private fun getAges() {
        ages {
            it.onData {

                executor.execute {
                    room.userDao().clearDB()
                    for (i in it.indices) {
                        val d = it[i]
                        room.userDao().insert(UsersData(i, d.age, d.firstName, d.lastName))
                    }
                }
                var i = 20
                while (i < 35) {
                    val count = it.count { it.age == i }
                    if (count > 0)
                        entryUser.add(PieEntry(count.toFloat(), i.toString()))
                    i++
                }
                pieChart(pie_age, entryUser, "years old")
            }
        }
    }

    private fun getWorkers() {
        workers {
            it.onData { list ->
                workersList.clear()
                list.data?.let { it1 -> workersList.addAll(it1) }
                for ((i, item) in list.data!!.withIndex()) {
                    workers.add(Entry(i + 1f, item.value.toFloat()))
                }
                var max = 0
                for (i in workersList.indices) {
                    if (max < workersList[i].value) max = workersList[i].value
                }

                setLineWorkers(workers, max)

                for (i in balanceList.indices) {
                    val k = balanceList[i].value.toFloat() / workersList[i].value.toFloat()
                    money.add(Entry(i.toFloat(), k))
                }
                hideProgress(moneyChartProgressBar)
                lineGraph(money)


            }
        }

    }

    private fun setLineWorkers(
        yValues: java.util.ArrayList<Entry>,
        max: Int
    ) {
        max_workers.text = max.toString()
        line_workers.setNoDataText("No Balance yet!")
        line_workers.isAutoScaleMinMaxEnabled = true
        line_workers.legend.isEnabled = true
        line_workers.animateX(1500)

        val lineDataSet = LineDataSet(yValues, "Workers")
        val lineData = LineData(lineDataSet)

        lineDataSet.color = Color.RED
        workers_chart.setTextColor(Color.RED)
        max_workers.setTextColor(Color.RED)
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawValues(false)

        lineDataSet.setDrawCircles(false)

        line_workers.axisRight.isEnabled = false

        line_workers.axisRight.setDrawLabels(false)
        line_workers.axisRight.setDrawAxisLine(false)
        line_workers.axisRight.setDrawGridLines(false)
        line_workers.description.isEnabled = false


        line_workers.xAxis.isEnabled = false

        line_workers.axisLeft.setDrawLabels(false)
        line_workers.axisLeft.setDrawAxisLine(false)
        line_workers.axisLeft.setDrawGridLines(false)
        line_workers.description.isEnabled = false

        line_workers.data = lineData
        line_workers.resetZoom()


    }

    private fun getTasks() {
        tasks { it ->
            it.onData { list ->

                tasksList.clear()
                list.data?.let { it1 -> tasksList.addAll(it1) }
                for ((i, item) in list.data!!.withIndex()) {
                    tasks.add(Entry(i + 1f, item.value.toFloat()))
                }
                var max = 0
                for (i in tasksList.indices) {
                    if (max < tasksList[i].value) max = tasksList[i].value

                }
                setLineTasks(tasks, max)
            }
        }
    }

    private fun setLineTasks(
        yValues: java.util.ArrayList<Entry>,
        max: Int
    ) {
        max_task.text = max.toString()
        line_tasks.setNoDataText("No Balance yet!")
        line_tasks.isAutoScaleMinMaxEnabled = true
        line_tasks.legend.isEnabled = true
        line_tasks.animateX(1500)

        val lineDataSet = LineDataSet(yValues, "Tasks")
        val lineData = LineData(lineDataSet)

        lineDataSet.color = Color.MAGENTA
        tasks_chart.setTextColor(Color.MAGENTA)
        max_task.setTextColor(Color.MAGENTA)
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawValues(false)

        lineDataSet.setDrawCircles(false)

        line_tasks.axisRight.isEnabled = false

        line_tasks.axisRight.setDrawLabels(false)
        line_tasks.axisRight.setDrawAxisLine(false)
        line_tasks.axisRight.setDrawGridLines(false)
        line_tasks.description.isEnabled = false


        line_tasks.xAxis.isEnabled = false

        line_tasks.axisLeft.setDrawLabels(false)
        line_tasks.axisLeft.setDrawAxisLine(false)
        line_tasks.axisLeft.setDrawGridLines(false)
        line_tasks.description.isEnabled = false

        line_tasks.data = lineData
        line_tasks.resetZoom()
    }

    private fun getBalance() {
        balance { it ->
            it.onData { list ->
                balanceList.clear()
                list.data?.let { it1 -> balanceList.addAll(it1) }
                for ((i, item) in list.data!!.withIndex()) {
                    balance.add(Entry(i + 1f, item.value.toFloat()))
                }
                var max = 0
                for (i in balanceList.indices) {
                    if (max < balanceList[i].value) max = balanceList[i].value
                }
                setLineBalance(balance, max)

            }
        }
    }

    private fun setLineBalance(
        yValues: java.util.ArrayList<Entry>,
        max: Int
    ) {
        max_balance.text = max.toString()
        line_balance.setNoDataText("No Balance yet!")
        line_balance.isAutoScaleMinMaxEnabled = true
        line_balance.legend.isEnabled = true
        line_balance.animateX(1500)

        val lineDataSet = LineDataSet(yValues, "Balance")
        val lineData = LineData(lineDataSet)

        lineDataSet.color = Color.GREEN
        balance_chart.setTextColor(Color.GREEN)
        max_balance.setTextColor(Color.GREEN)
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawValues(false)

        lineDataSet.setDrawCircles(false)

        line_balance.axisRight.isEnabled = false

        line_balance.axisRight.setDrawLabels(false)
        line_balance.axisRight.setDrawAxisLine(false)
        line_balance.axisRight.setDrawGridLines(false)
        line_balance.description.isEnabled = false


        line_balance.xAxis.isEnabled = false

        line_balance.axisLeft.setDrawLabels(false)
        line_balance.axisLeft.setDrawAxisLine(false)
        line_balance.axisLeft.setDrawGridLines(false)
        line_balance.description.isEnabled = false

        line_balance.data = lineData
        line_balance.resetZoom()
    }

    private fun workers(block: (ResultData<ResponseData<List<ValueData>>>) -> Unit) {
        executor.execute {
            try {
                val res = api.workers().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgress(workersChartProgressBar)
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgress(workersChartProgressBar)
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgress(workersChartProgressBar)
                            block(ResultData.data(res))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgress(workersChartProgressBar)
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun ages(block: (ResultData<List<UserData>>) -> Unit) {
        executor.execute {
            try {
                val res = api.users().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgress(usersChartProgressBar)
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgress(usersChartProgressBar)
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {

                        runOnUiThread {
                            hideProgress(usersChartProgressBar)
                            block(ResultData.data(res.data ?: emptyList()))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgress(usersChartProgressBar)
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun manufactures(block: (ResultData<List<ProductData>>) -> Unit) {
        executor.execute {
            try {
                val res = api.products().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgress(productsChartProgressBar)
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgress(productsChartProgressBar)
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgress(productsChartProgressBar)
                            block(ResultData.data(res.data ?: emptyList()))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgress(productsChartProgressBar)
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun tasks(block: (ResultData<ResponseData<List<ValueData>>>) -> Unit) {
        executor.execute {
            try {
                val res = api.tasks().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgress(tasksChartProgressBar)
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgress(tasksChartProgressBar)
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgress(tasksChartProgressBar)
                            balanceChartProgressBar.visibility = View.GONE
                            block(ResultData.data(res))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgress(tasksChartProgressBar)
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun balance(block: (ResultData<ResponseData<List<ValueData>>>) -> Unit) {
        executor.execute {
            try {
                val res = api.balance().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgressAll()
                            hideProgress(balanceChartProgressBar)
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgressAll()
                            hideProgress(balanceChartProgressBar)
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            proDialog.dismiss()
                            aaa()
                            hideProgress(balanceChartProgressBar)
                            block(ResultData.data(res))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgressAll()
                    showMessage("Server is not found")
                }
            }
        }
    }


    private fun allBalance(block: (ResultData<ResponseData<List<ValueData>>>) -> Unit) {
        showProgress()
        executor.execute {
            try {
                val res = api.allBalance().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            block(ResultData.data(res))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgressDialog()
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun allTasks(block: (ResultData<ResponseData<List<ValueData>>>) -> Unit) {
        showProgress()
        executor.execute {
            try {
                val res = api.allTasks().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            block(ResultData.data(res))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgressDialog()
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun allWorkers(block: (ResultData<ResponseData<List<ValueData>>>) -> Unit) {
        executor.execute {
            try {
                val res = api.allWworkers().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            block(ResultData.data(res))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgressDialog()
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun allAges(block: (ResultData<List<UserData>>) -> Unit) {
        showProgress()
        executor.execute {
            try {
                val res = api.allUsers().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                           hideProgressDialog()
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {

                        runOnUiThread {
                            hideProgressDialog()
                            block(ResultData.data(res.data ?: emptyList()))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgressDialog()
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun allManufactures(block: (ResultData<List<ProductData>>) -> Unit) {
        showProgress()
        executor.execute {
            try {
                val res = api.allProducts().execute().body()
                when {
                    res == null -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage("Null data came from server")
                        }
                    }
                    res.status == "ERROR" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            showMessage(res.message)
                        }
                    }
                    res.status == "OK" -> {
                        runOnUiThread {
                            hideProgressDialog()
                            block(ResultData.data(res.data ?: emptyList()))
                        }
                    }
                }

            } catch (e: Throwable) {
                runOnUiThread {
                    hideProgressDialog()
                    showMessage("Server is not found")
                }
            }
        }
    }

    private fun hideProgressAll() {
        proDialog.dismiss()
        moneyChartProgressBar.visibility = View.GONE
        balanceChartProgressBar.visibility = View.GONE
        tasksChartProgressBar.visibility = View.GONE
        workersChartProgressBar.visibility = View.GONE
        usersChartProgressBar.visibility = View.GONE
        productsChartProgressBar.visibility = View.GONE
    }

    private fun aaa() {
        getTasks()
        getWorkers()

        getAges()
        getManufactures()
    }
    private fun hideProgressDialog() {
        proDialog.dismiss()
    }

    private fun hideProgress(view: ProgressBar) {
        view.visibility = View.GONE
    }

    private fun showMessage(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

}
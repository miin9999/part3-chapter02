package fastcampus.aop.part3.chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy{
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy{
        findViewById(R.id.progressBar)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews(){

        viewPager.setPageTransformer { page, position -> // 어떤 페이지인지, 포지션이 어떤지
            when{
                position.absoluteValue >= 1F ->{
                    page.alpha = 0F
                }
                position == 0F ->{
                    page.alpha =1F
                }
                else->{ // 0에 인접한 화면들이 어떻게 보여질지를 처리하는 곳
                    page.alpha = 1F - 2 * position.absoluteValue

                }
            }
        }
    }

    private fun initData(){
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync( // 비동기로 세팅
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0 // 시간 단축 시킴, 서버에서 블락하지 않는 이상 곧바로 fetch가 진행됨
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // 페치와 액티베잇이라는 Task작업을 완료했을 때 들어옴

            progressBar.visibility = View.GONE
            if(it.isSuccessful){
                val quotes = parseQuotesJson(remoteConfig.getString("quotes")) // key값은 파베에서 설정한 값으로
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes,isNameRevealed)


            }
        }
    }

    private fun parseQuotesJson(json: String) : List<Quote>{

        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let{
                jsonList = jsonList + it // 뒤에 덧붙이고 리스트에 다시 추가하는 느낌
            }
        }

        return jsonList.map{
            Quote(
                it.getString("quote"),
                it.getString("name")
            )
        }

    }

    private fun displayQuotesPager(quotes:List<Quote>,isNameRevealed:Boolean){

        val adapter = QuotePagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )


        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount/2,false)
    }

}
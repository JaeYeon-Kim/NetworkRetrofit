package com.kjy.networkretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjy.networkretrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터 요청을 위한 리사이클러뷰 어댑터를 불러옴.
        val adapter = CustomAdapter()
        binding.recyclerView.adapter = adapter

        // 레이아웃 매니저 연결.
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        /*Retrofit.Builder()를 사용해서 레트로핏을 생성하고 retrofit 변수에 담음.
          baseUrl이 되는 Github의 도메인 주소와 JSON 데이터를 앞에서 생성한 Repository 클래스의 컬렉션으로
          변환해주는 컨버터를 입력하고 Builder() 메서드로 생성
         */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 요청 버튼 클릭시 Retrofit으로 데이터를 불러오고 어댑터를 셋팅
        // 정의한 인터페이스를 파라미터로 넘겨주면 실행 가능한 서비스 객체를 생성해서 반환.
        binding.buttonRequest.setOnClickListener {
            /* create() 메서드 : 인터페이스를 실행 가능한 서비스 객체로 만듬
            enqueue() 메서드 : users() 메서드 안에 비동기 통신으로 데이터를 가져오는 enqueue() 메서드를 추가.
            호출되면 통신이 시작.
             */
            val githubService = retrofit.create(GithubService::class.java)
            // enqueue() 메서드 호출 후 github API 서버로부터 응답을 받으면 enqueue() 안에 작성하는 콜백 인터페이스
            // 가 작동. retrofit2 import
            githubService.users().enqueue(object: Callback<Repository> {
                override fun onFailure(call: Call<Repository>, t: Throwable) {

                }

                // 통신이 성공적이면 호출 함.
                // 두번째 파라미터인 response의 body() 메서드를 호출시 서버로부터 전송된 데이터를 꺼낼 수 있음.
                // 데이터를 형변환 후에 리스트에 담음
                override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                    adapter.userList = response.body() as Repository
                    // notifyDataSetChanged() 호출 시 리사이클러뷰에 변경된 사항이 반영.
                    adapter.notifyDataSetChanged()
                }


            })
        }
    }

}

// Retrofit 사용을 위해 인터페이스를 정의하기 위해서 탑레벨에 인터페이스 생성
// Retrofit 인터페이스는 호출 방식, 주소, 데이터 등을 지정.
// Retrofit 라이브러리는 인터페이스를 해석하여 HTTP 통신을 처리함.
interface GithubService {
    // Github API를 호출할 users 메서드를 만들고 @GET 애노테이션을 사용해서 요청 주소를 설정.(주소에는 Github 도메인은 제외하고 작성)
    // 반환값은 Call<데이터 클래스> 형태로 작성.
    // Retrofit은 이렇게 만들어진 인터페이스에 지정된 방식으로 서버와 통신하고 데이터를 가져옴.
    @GET("users/Kotlin/repos")
    fun users(): Call<Repository>           // retrofit2 import

}
package com.kjy.networkretrofit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kjy.networkretrofit.databinding.ItemRecyclerBinding

// 사용자 정보를 목록으로 보여주기 위한 리사이클러뷰 어댑터 생성
// RecyclerView.Adapter를 상속 받고 제네릭으로 Holder를 지정.
class CustomAdapter: RecyclerView.Adapter<Holder>() {
    // 어댑터에서 사용할 데이터 컬렉션을 변수로 만들어 놓음.
    // 사용할 데이터 셋은 JSON으로 불러올 시에 자동으로 생성되었던 Repository
    var userList: Repository? = null

    // 홀더를 생성하는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    // 목록에 출력되는 총 아이템 개수
    override fun getItemCount(): Int {
        return userList?.size?:0
    }

    // 실제 목록에 나타내어지는 아이템을 그려주는 메서드
    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 현 위치의 사용자 데이터를 userList에서 가져오고 홀더의 setUser()메서드에 넘겨줌
        val user = userList?.get(position)
        holder.setUser(user)
    }

}


// 홀더의 생성자에서 바인딩을 전달받고 상속받은 ViewHolder에는 binding.root를 전달.
class Holder(val binding: ItemRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    // userList가 nullable 이기 때문에 user파라미터도 nullable로 설정되어야 함.
    fun setUser(user: RepositoryItem?) {
        // 홀더가 가지고 있는 아이템 레이아웃에 데이터를 하나씩 세팅해줌.
        // 데이터 각각 아바타 주소: user.owner.avatar_url, 사용자 이름: user.name, 사용자 ID: user.node_id
        user?.let {
            binding.textName.text = user.name
            binding.textId.text = user.node_id
            Glide.with(binding.imageAvatar).load(user.owner.avatar_url).into(binding.imageAvatar)
        }
    }
}
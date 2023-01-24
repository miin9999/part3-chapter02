package fastcampus.aop.part3.chapter02

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotePagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
) : RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote,parent,false)
        )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size
        holder.bind(quotes[actualPosition],isNameRevealed)
    }

    override fun getItemCount() = Int.MAX_VALUE

    class QuoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote:Quote, isNameRevealed: Boolean){
            quoteTextView.text = "\"${quote.quote}\"" // 양옆에 따옴표 넣어주기

            if(isNameRevealed){
                nameTextView.text = "- ${quote.name}" // name 앞에 - 넣어주기
                nameTextView.visibility = View.VISIBLE // 리사이클러뷰다보니까 재사용을 하다보니
                // 이걸 안해주면 어쩔 땐 안보일 수도 있으니 추가한 코드
            }else{
                nameTextView.visibility = View.GONE
            }


        }
    }
}
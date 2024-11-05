package com.example.hkunexus.ui.homePages.mygroups
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.hkunexus.R
import com.example.hkunexus.data.model.dto.ClubDto
import android.view.ViewGroup as ViewGroup

class GroupListAdapter(private val dataSet: List<ClubDto>) :
    RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    private var goToPostPage: (Int) -> Unit = { postID: Int -> }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //var clubImage: TextView = view.findViewById<TextView>(R.id.club_banner_image)
        var clubName: TextView = view.findViewById<TextView>(R.id.club_name)
        var clubDescription: TextView = view.findViewById<TextView>(R.id.club_description)
        var cardView: CardView = view.findViewById<CardView>(R.id.club_card)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.explore_clubs_card, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val club = dataSet[position]
        //val clubImage = club.xxxxx

        viewHolder.clubName.text = club.clubName
        viewHolder.clubDescription.text = club.clubDesc // Use the local date part

        viewHolder.cardView.setOnClickListener {
            goToPostPage(position)
        }
    }

    override fun getItemCount() = dataSet.size

    fun setPostPageCallBack(callback: (Int) -> Unit) {
        goToPostPage = callback
    }
}

package com.example.tekapps

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class home_Adapter(var lst:List<catogry>,var context: Context,var acti:Int): RecyclerView.Adapter<home_Adapter.viewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view:View
        if(acti==0)
        {view = LayoutInflater.from(parent.context).inflate(R.layout.home_page_view,parent,false)}
        else{
           view= LayoutInflater.from(parent.context).inflate(R.layout.app_list_view,parent,false)
        }
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return lst.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.tv_title.setText(lst[position].name)
        Glide.with(context).load(lst[position].purl).into(holder.iv_img)
        holder.itemView.setOnClickListener(View.OnClickListener {
            if (lst[position].acti==0)
            {
                var intent = Intent(context, appListActivity::class.java)
                intent.putExtra("path", lst[position].idx)
                context.startActivity(intent)
            }
            else{
                var intent = Intent(context, AppActivity::class.java)
                intent.putExtra("name", lst[position].name)
                intent.putExtra("img", lst[position].purl)
                intent.putExtra("url", lst[position].idx)
                context.startActivity(intent)
            }
        }
        )
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var iv_img = itemView.findViewById<ImageView>(R.id.iv_home_view_img)
        var tv_title = itemView.findViewById<TextView>(R.id.tv_home_view)
    }
}


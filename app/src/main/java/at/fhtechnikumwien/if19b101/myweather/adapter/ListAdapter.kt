package at.fhtechnikumwien.if19b101.myweather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.fhtechnikumwien.if19b101.myweather.R
import at.fhtechnikumwien.if19b101.myweather.data.WeatherItem
import kotlin.math.roundToInt

class ListAdapter (items : List<WeatherItem> = listOf(), var measurementUnit: Boolean = true) : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    companion object{
        private const val TYPE_TOP = 0
        private const val TYPE_OTHERS = 1
    }
    var items = items
    set(value){
        field = value
        notifyDataSetChanged()
    }
    var itemClickHandler : ((WeatherItem) -> Unit)? = null

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val itemTextViewDate = itemView.findViewById<TextView>(R.id.tv_item_date)
        private val itemTextViewIcon = itemView.findViewById<ImageView>(R.id.tv_item_icon)
        private val itemTextViewTemperature = itemView.findViewById<TextView>(R.id.tv_item_temperature)
        private val itemTextViewCondition = itemView.findViewById<TextView>(R.id.tv_item_condition)

        //initialize on click event for each list item
        init {
            itemView.setOnClickListener {
                itemClickHandler?.invoke(items[absoluteAdapterPosition])
            }
        }

        fun bind(index : Int){
            itemTextViewDate.text = items[index].date

            //choose the right image (could be done more compact)
            var tmpIconInt: Int? = null
            when (items[index].icon) {
                "01d" -> { tmpIconInt = R.drawable._01d}
                "02d" -> { tmpIconInt = R.drawable._02d}
                "03d" -> { tmpIconInt = R.drawable._03d}
                "04d" -> { tmpIconInt = R.drawable._04d}
                "09d" -> { tmpIconInt = R.drawable._09d}
                "10d" -> { tmpIconInt = R.drawable._10d}
                "11d" -> { tmpIconInt = R.drawable._11d}
                "13d" -> { tmpIconInt = R.drawable._13d}
                "50d" -> { tmpIconInt = R.drawable._50d}
                "01n" -> { tmpIconInt = R.drawable._01n}
                "02n" -> { tmpIconInt = R.drawable._02n}
                "03n" -> { tmpIconInt = R.drawable._03n}
                "04n" -> { tmpIconInt = R.drawable._04n}
                "09n" -> { tmpIconInt = R.drawable._09n}
                "10n" -> { tmpIconInt = R.drawable._10n}
                "11n" -> { tmpIconInt = R.drawable._11n}
                "13n" -> { tmpIconInt = R.drawable._13n}
                "50n" -> { tmpIconInt = R.drawable._50n}
            }
            if (tmpIconInt != null) {
                itemTextViewIcon.setImageResource(tmpIconInt)
            }

            //first list item first letter uppercase
            if(index == TYPE_TOP){
                itemTextViewCondition.text = items[index].condition.capitalize()
            }

            if(!measurementUnit){
                //false == imperial
                //°F = C° * 1,8 + 32
                ((((items[index].temperature * 1.8 + 32) * 100.0).roundToInt() / 100.0).toString() + " °F")
                    .also {
                        itemTextViewTemperature.text = it
                    }
            } else {
                //true == metric
                itemTextViewTemperature.text = items[index].temperature.toString() + " °C"
            }
        }
    }

    //define different viewType for first list element
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_TOP else TYPE_OTHERS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        //inflate different xml file for first list item
        val layoutIdForListItem =
            if (viewType == TYPE_TOP)
                R.layout.list_item_top
            else
                R.layout.list_item
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layoutIdForListItem, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int){
        holder.bind(position)
    }

    //change measurement unit boolean and update list data
    fun reload(measurementUnit: Boolean) {
        this.measurementUnit = measurementUnit
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
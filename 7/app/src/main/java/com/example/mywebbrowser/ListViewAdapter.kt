
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mywebbrowser.ListViewItem
import com.example.mywebbrowser.R

class ListViewAdapter(val context: Context, val items: ArrayList<ListViewItem>): BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }
    override fun getItem(position: Int): Any{
        return items[position]
    }
    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_list_item, null)

        val Name = view.findViewById<TextView>(R.id.tvName)
        val Phone = view.findViewById<TextView>(R.id.tvPhone)
        val list = items[position]
        Name.text = list.name
        Phone.text = list.phone

        return view
    }
}


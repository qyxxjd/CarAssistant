package com.classic.assistant.car.ui.add

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.classic.assistant.car.R
import com.classic.assistant.car.app.App
import com.classic.assistant.car.extension.applyGridConfig
import com.classic.assistant.car.extension.commonGridDividerRes
import com.classic.assistant.car.extension.px

/**
 * 文本选择
 *
 * @author Classic
 * @version v2.0, 2018/02/13 上午10:11
 */
class TextChooseFragment : DialogFragment() {
    private val label = TextChooseFragment::class.java.simpleName

    private var title = ""
    private var hint = ""
    private var columnCount = 3

    private var titleLayout: View? = null
    private var titleView: TextView? = null
    private var hintView: TextView? = null
    private var rv: RecyclerView? = null
    private var list: ArrayList<String> = arrayListOf()
    private var listener: TextChooseListener? = null

    fun title(title: String): TextChooseFragment {
        this.title = title
        return this
    }

    fun hint(hint: String): TextChooseFragment {
        this.hint = hint
        return this
    }

    fun columnCount(count: Int): TextChooseFragment {
        this.columnCount = count
        return this
    }

    fun items(list: List<String>): TextChooseFragment {
        this.list.clear()
        this.list.addAll(list)
        return this
    }

    fun listener(listener: TextChooseListener): TextChooseFragment {
        this.listener = listener
        return this
    }

    fun show(fm: FragmentManager) {
        show(fm, label)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.CarDialog)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_text_choose, container, false)
        rv = view.findViewById(R.id.text_choose_rv)
        titleLayout = view.findViewById(R.id.text_choose_title_layout)
        titleView = view.findViewById(R.id.text_choose_title)
        hintView = view.findViewById(R.id.text_choose_hint)
        rv?.applyGridConfig(columnCount, decoration = view.context.commonGridDividerRes(R.dimen.divider, R.color.divider))
        dialog?.setCanceledOnTouchOutside(true)
        isCancelable = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (title.isNotEmpty()) {
            titleLayout?.visibility = View.VISIBLE
            titleView?.text = title
            if (hint.isNotEmpty()) {
                hintView?.visibility = View.VISIBLE
                hintView?.text = hint
            }
        }
        rv?.adapter = Adapter()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val margin = App.get().context.px(R.dimen.margin_large)
            val width = displayMetrics.widthPixels - margin * 2
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onDestroyView() {
        rv?.adapter = null
        super.onDestroyView()
    }

    private inner class Adapter : RecyclerView.Adapter<Holder>() {
        override fun getItemCount(): Int = list.size
        private fun getItem(position: Int) = list[position]
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.common_item_text, parent, false)
            return Holder(view)
        }
        override fun onBindViewHolder(holder: Holder, position: Int) {
            with(holder) {
                title.text = getItem(position)
                itemView.setOnClickListener {
                    if (null != listener && adapterPosition < list.size) {
                        listener!!.onChoose(adapterPosition, list[adapterPosition])
                    }
                    dismiss()
                }
            }
        }
    }
    private class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.common_item_title)
    }
}

interface TextChooseListener {
    fun onChoose(index: Int, text: String)
}

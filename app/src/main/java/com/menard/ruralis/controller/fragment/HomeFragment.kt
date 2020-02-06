package com.menard.ruralis.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.menard.ruralis.R
import com.menard.ruralis.model.KnowsIt
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {


    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private var index: Int = 0

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        textView = view.findViewById(R.id.knows_it_txt)
        imageView = view.findViewById(R.id.knows_it_img)

//        val knowsIt = getKnowsIt()
//        textView.text = knowsIt.info
//        imageView.setImageResource(knowsIt.drawable_id!!)

        return view
    }


//    private fun getKnowsIt(): KnowsIt{
//
////        val list = ArrayList<KnowsIt>()
////        val knowsIt1 = KnowsIt(0, "Les poules ne pondent pas un oeuf par jour, mais un toutes les 26 heures. De plus, plus la durée du jour décroit, moins elles pondent, jusqu'à s'arrêter complètement en Hiver. C'est pour cela que les éleveurs utilisent un système d'éclairage, pour nous permettre de manger des oeufs toute l'année.", R.drawable.egg)
////        val knowsIt2 = KnowsIt(1, "Une cinquantaine de races de bovin sont présentes sur le territoire français", R.drawable.cow)
////        val knowsIt3 = KnowsIt(2, "Il peut arriver que vous surpreniez votre lapin en train de manger ses crottes. Rassurez-vous, ça n'a rien de bizarre. Le lapin est un coprophage. Afin de palier au manque d'efficacité de son système digestif, il doit ingérer deux fois ses aliments pour les digérer entièrement.", R.drawable.rabbit)
////        list.add(knowsIt1)
////        list.add(knowsIt2)
////        list.add(knowsIt3)
////        list.shuffle()
//
////        if(index == list.size){
////            index = 0
////        }
////        return list[index++]
//
//    }

}
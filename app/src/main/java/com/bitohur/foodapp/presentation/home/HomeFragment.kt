package com.bitohur.foodapp.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitohur.foodapp.R
import com.bitohur.foodapp.data.MenuDataSource
import com.bitohur.foodapp.data.MenuDataSourceImpl
import com.bitohur.foodapp.databinding.FragmentHomeBinding
import com.bitohur.foodapp.model.Menu
import com.bitohur.foodapp.presentation.home.adapter.AdapterLayoutMode
import com.bitohur.foodapp.presentation.home.adapter.MenuListAdapter

class FragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var isPlaying = false

    private val dataSource: MenuDataSource by lazy {
        MenuDataSourceImpl()
    }

    private val adapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayoutMode.LINEAR) { menu: Menu ->
            navigateToDetail(menu)
        }
    }

    private fun navigateToDetail(menu: Menu) {
        findNavController().navigate(
            FragmentHomeDirections.actionHomeFragmentToMenuDetailFragment(menu)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpList()
        setClickListener()
    }

    private var isGridMode = false

    private fun setClickListener() {
        binding.ibButtonListGrid.setOnClickListener {
            val layoutManager = binding.rvMenu.layoutManager as? GridLayoutManager
            layoutManager?.spanCount = if (isGridMode) 2 else 1
            adapter.adapterLayoutMode = if (isGridMode) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            adapter.refreshList()

            if (isPlaying) {
                binding.ibButtonListGrid.setImageResource(R.drawable.ic_list)
            } else {
                binding.ibButtonListGrid.setImageResource(R.drawable.ic_grid)
            }

            isGridMode = !isGridMode
            isPlaying = !isPlaying
        }
    }



    private fun setUpList(){
        val span = if (adapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@FragmentHome.adapter
        }
        adapter.submitData(dataSource.getMenus())
    }

}
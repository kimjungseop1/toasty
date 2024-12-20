package com.syncrown.arpang.ui.component.home.tab1_home.life2cut.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.syncrown.arpang.network.ArPangRepository
import com.syncrown.arpang.network.NetworkResult
import com.syncrown.arpang.network.model.RequestTemplateListDto
import com.syncrown.arpang.network.model.ResponseTemplateListDto
import com.syncrown.arpang.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class EditPrint2CutImageViewModel : BaseViewModel() {
    private val arPangRepository: ArPangRepository = ArPangRepository()

    /***********************************************************************************************
     * 템플릿 아이콘 리스트
     **********************************************************************************************/
    private val templateIconResponseLiveData: LiveData<NetworkResult<ResponseTemplateListDto>> =
        arPangRepository.templateListLiveDataRepository

    fun templateIcons(requestTemplateListDto: RequestTemplateListDto) {
        viewModelScope.launch {
            arPangRepository.requestTemplateList(requestTemplateListDto)
        }
    }

    fun templateIconResponseLiveData(): LiveData<NetworkResult<ResponseTemplateListDto>> {
        return templateIconResponseLiveData
    }
}
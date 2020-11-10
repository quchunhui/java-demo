package rexel.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rexel.common.Constants;
import rexel.common.Utils;
import rexel.entity.CollectSwitchEntity;
import rexel.repository.CollectSwitchRepository;

@Transactional
@Service
@Slf4j
public class CollectSwitchServiceImpl implements CollectSwitchService{
    private CollectSwitchRepository collectSwitchRepository;
    private Utils utils;

    @Autowired
    public void setCollectSwitchRepository(CollectSwitchRepository collectSwitchRepository) {
        this.collectSwitchRepository = collectSwitchRepository;
    }

    @Autowired
    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    @Override
    public JSONObject update(int collect) {
        //清空所有数据
        collectSwitchRepository.truncate();

        //重新出入新的数据
        CollectSwitchEntity entity = new CollectSwitchEntity();
        entity.setCollect(collect);
        collectSwitchRepository.insert(entity);

        return utils.responseJson(Constants.RESPONSE_OK, "succeeded.");
    }
}

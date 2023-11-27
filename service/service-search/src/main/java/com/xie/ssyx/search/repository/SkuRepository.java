package com.xie.ssyx.search.repository;

import com.xie.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @title: SkuRepository
 * @Author Xie
 * @Date: 2023/11/7 22:08
 * @Version 1.0
 */
@Component
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {

    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);

    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);

    Page<SkuEs> findByKeywordAndWareId(String keyword, Long wareId, Pageable pageable);
}

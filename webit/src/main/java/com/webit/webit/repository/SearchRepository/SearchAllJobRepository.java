package com.webit.webit.repository.SearchRepository;


import com.webit.webit.dto.response.PageResponse;
import com.webit.webit.model.Job;
import com.webit.webit.util.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchAllJobRepository {

    private final MongoTemplate mongoTemplate;

    public PageResponse<?> getAllJobs(int pageNo, int pageSize, String keyword, String location, String category, Type type, BigDecimal minSalary, BigDecimal maxSalary) {

        Query query = new Query();
        Criteria criteria = new Criteria();

        boolean hasCriteria = false;

        // Keyword (tìm trong title)
        if (keyword != null && !keyword.isEmpty()) {
            criteria.and("title").regex(keyword, "i"); // "i" = ignore case
            hasCriteria = true;
        }

        // Location
        if (location != null && !location.isEmpty()) {
            criteria.and("location").regex(location, "i");
            hasCriteria = true;
        }

        // Category
        if (category != null && !category.isEmpty()) {
            criteria.and("category").regex(category, "i");
            hasCriteria = true;
        }

        // Type (FULL_TIME, PART_TIME...)
        if (type != null) {
            criteria.and("type").is(type);
            hasCriteria = true;
        }

        //  Lọc theo khoảng lương giao nhau
        if (minSalary != null && maxSalary != null) {
            criteria.andOperator(
                    Criteria.where("salaryMin").lte(maxSalary),
                    Criteria.where("salaryMax").gte(minSalary)
            );
            hasCriteria = true;
        } else if (minSalary != null) {
            criteria.and("salaryMax").gte(minSalary);
            hasCriteria = true;
        } else if (maxSalary != null) {
            criteria.and("salaryMin").lte(maxSalary);
            hasCriteria = true;
        }

        query.addCriteria(criteria);

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        query.with(pageable);

        // Lấy dữ liệu
        List jobs = mongoTemplate.find(query, Job.class);

        Query countQuery = new Query();
        if (hasCriteria) {
            countQuery.addCriteria(criteria);
        }
        long totalElements = mongoTemplate.count(countQuery, Job.class);

        Page<?> page = new PageImpl<Object>(jobs, pageable, totalElements);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .items(page.stream().toList())
                .build();
    }
}


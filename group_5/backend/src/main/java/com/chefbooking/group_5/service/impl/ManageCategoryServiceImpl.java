package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.AdminAddNewCategoryRequest;
import com.chefbooking.group_5.dto.response.*;
import com.chefbooking.group_5.entity.Category;
import com.chefbooking.group_5.repository.ManageCategoryRepository;
import com.chefbooking.group_5.service.ManageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageCategoryServiceImpl implements ManageCategoryService {

    private final ManageCategoryRepository manageMenuRepository;

    // Danh sách từ nhạy cảm (tự thêm/bớt tuỳ quy định của nhóm)
    private static final List<String> SENSITIVE_WORDS = List.of(
            "địt", "lồn", "cặc", "đụ", "dm", "dmm",
            "fuck", "bitch", "shit"
    );

    @Override
    public List<ManageCategoryResponse> getAllCategoryByAdmin() {
        return manageMenuRepository.getAllCategories();
    }

    @Override
    public List<ManageCategoryResponse> addNewCategoryByAdmin(AdminAddNewCategoryRequest request) {
        String rawName = request.getCategoryName();
        String rawDescription = request.getDescription();

        //Chuẩn hoá tên
        String normalizedName = normalizeCategoryName(rawName);

        //Lọc từ
        if (checkBadWords(normalizedName) || checkBadWords(rawDescription)) {
            throw new IllegalArgumentException("Category Name/Description chứa từ ngữ không phù hợp");
        }

        //Check trùng tương tự trong DB
        String normalizedKey = normalizedName.replaceAll("\\s+", "").toLowerCase();
        if (manageMenuRepository.existsBySimilarName(normalizedKey)) {
            throw new IllegalArgumentException("Category đã tồn tại hoặc tương tự, không thể thêm trùng");
        }

        Category category = new Category();
        category.setCategoryName(normalizedName);
        category.setDescription(rawDescription);
        manageMenuRepository.save(category);

        return manageMenuRepository.getAllCategories();
    }

    private boolean checkBadWords(String text) {
        if (text == null) return false;
        String lower = text.toLowerCase();
        for (String bad : SENSITIVE_WORDS) {
            if (lower.contains(bad)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeCategoryName(String input) {
        if (input == null) return "";
        // Xoá khoảng trắng đầu, cuối -> Chuyển thành thường -> Chia thành các part tách nhau bởi khoảng trắng
        String[] words = input.trim().replaceAll("\\s+", " ").toLowerCase().split(" ");
        // Viết hoa chữ cái đầu mỗi từ
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue; // bỏ qua nếu rỗng
            char firstChar = Character.toUpperCase(word.charAt(0)); // chữ cái đầu viết hoa
            String capitalizedWord = firstChar + word.substring(1); // nối với phần còn lại
            result.append(capitalizedWord).append(" ");
        }
        return result.toString().trim();
    }

    @Override
    public List<ManageCategoryResponse> searchCategoryByAdmin(String categoryName) {
        return manageMenuRepository.findCategoriesByName(categoryName);
    }

    @Override
    public ManageCategoryResponse updateCategoryByAdmin(Integer categoryId, AdminAddNewCategoryRequest request) {
        Category category = manageMenuRepository.findById(categoryId).orElse(null);
        if(category != null) {
            if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
                category.setCategoryName(request.getCategoryName().trim());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                category.setDescription(request.getDescription().trim());
            }
            manageMenuRepository.save(category);
        }
        return getCategoryDetailByAdmin(categoryId);
    }

    @Override
    public ManageCategoryResponse getCategoryDetailByAdmin(Integer categoryId) {
        return manageMenuRepository.getCategoryDetailByAdmin(categoryId);
    }
}

package com.chefbooking.group_5.service.impl;

import com.chefbooking.group_5.dto.request.AdminUpdateProfileRequest;
import com.chefbooking.group_5.dto.request.AdminUpdateStatusCVRequest;
import com.chefbooking.group_5.dto.response.*;
import com.chefbooking.group_5.entity.ChefProfile;
import com.chefbooking.group_5.entity.User;
import com.chefbooking.group_5.entity.UserRole;
import com.chefbooking.group_5.repository.ManageChefRepository;
import com.chefbooking.group_5.repository.UserRepository;
import com.chefbooking.group_5.service.ManageChefService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageChefServiceImpl implements ManageChefService {

    private final ManageChefRepository chefProfileRepository;
    private final UserRepository userRepository;

    @Override
    public ChefPageResponse getChefProfileCVResponse(Byte status, int page, int size) {
        List<ChefProfile> listChef = chefProfileRepository.findByStatus(status);
        List<ChefProfileCVResponse> listChefCV= new ArrayList<>();
        for (ChefProfile chef : listChef){
            List<ChefCertificateResponse> listCertiOfChef = chefProfileRepository.getChefCertificateResponse(chef.getChefId());
            listChefCV.add(getChefProfileCVResponse(chef, listCertiOfChef));
        }

        // Phân trang thủ công (in-memory)
        int total = listChefCV.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int from = page * size;
        int to = Math.min(from + size, total);

        List<ChefProfileCVResponse> pagedList = (from < total)
                ? listChefCV.subList(from, to)
                : new ArrayList<>();
        // Gói kết quả trả về
        ChefPageResponse response = new ChefPageResponse();
        response.setUsers(pagedList);
        response.setCurrentPage(page);
        response.setPageSize(size);
        response.setTotalElements(total);
        response.setTotalPages(totalPages);

        return response;
    }

    private ChefProfileCVResponse getChefProfileCVResponse(ChefProfile chefProfile, List<ChefCertificateResponse> chefCertificateResponse) {
        User user = chefProfile.getUser();
        ChefProfileCVResponse c = new ChefProfileCVResponse();
        c.setChefId(chefProfile.getChefId());
        c.setBio(chefProfile.getBio());
        c.setExperienceYears(chefProfile.getExperienceYears());
        c.setStatus(chefProfile.getStatus());
        c.setSpecialty(chefProfile.getSpecialty());
        c.setCertificates(chefCertificateResponse);
        c.setFullName(user.getFullName());
        c.setEmail(user.getEmail());
        c.setLocation(user.getAddress());
        c.setProfileImageUrl(user.getProfileImageUrl());
        return c;
    }

    @Override
    public ChefProfileCVResponse getChefCVDetail(Integer userID) {
        ChefProfile chefProfile = chefProfileRepository.findById(userID).orElse(null);
        ChefProfileCVResponse chefCV;
        if (chefProfile != null){
            List<ChefCertificateResponse> listCertiOfChef = chefProfileRepository.getChefCertificateResponse(chefProfile.getChefId());
            return getChefProfileCVResponse(chefProfile, listCertiOfChef);
        }
        return null;
    }

    @Transactional
    @Override
    public ChefProfileCVResponse updateStatusCVAndRoleByAdmin(Integer chefId, AdminUpdateStatusCVRequest request) {
        ChefProfile chefProfile = chefProfileRepository.findById(chefId).orElse(null);
        ChefProfileCVResponse chefCV;
        if(chefProfile != null){
            Byte status = request.getStatus();
            if (status != null) {
                chefProfile.setStatus(request.getStatus());
                chefProfileRepository.save(chefProfile);
                if (status == 2) {
                    User user = chefProfile.getUser();
                    userRepository.updateRoleByUserId(user.getUserId(), 2);
                    userRepository.save(user);
                }
                if (status == 3) {
                    User user = chefProfile.getUser();
                    userRepository.updateRoleByUserId(user.getUserId(), 4);
                    userRepository.save(user);
                }
            }
            List<ChefCertificateResponse> listCertiOfChef = chefProfileRepository.getChefCertificateResponse(chefProfile.getChefId());
            return getChefProfileCVResponse(chefProfile, listCertiOfChef);
        }
        return null;
    }
}

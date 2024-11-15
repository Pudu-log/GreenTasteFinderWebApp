package com.example.demo.controller.admin;


import com.example.demo.dto.MemberDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.service.AdminService;
import com.example.demo.type.ResponseStatus;
import com.example.demo.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    AdminService adminService;

    @Autowired
    public AdminApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/member-list")
    public ResponseEntity<ApiResponse<List<MemberDto>>> getMemberList() {

        List<MemberDto> resultList = adminService.memberList();
        return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, resultList));
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<ApiResponse<String>> deleteMember(@PathVariable String id) {
        int result = adminService.deleteMember(id);
        if (result > 0) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "삭제 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.BAD_REQUEST, "삭제 실패"));
        }
    }

    @GetMapping("/room-list")
    public ResponseEntity<ApiResponse<List<RoomDto>>> getRoomList() {

        List<RoomDto> resultList = adminService.roomList();
        return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, resultList));
    }

    @DeleteMapping("/room/delete/{roomCode}")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable String roomCode) {
        int result = adminService.deleteRoom(roomCode);
        if (result > 0) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "삭제 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.BAD_REQUEST, "삭제 실패"));
        }
    }

    @PostMapping("/room/insert")
    public ResponseEntity<ApiResponse<String>> insertRoom(@RequestBody RoomDto roomDto) {
        System.out.println(roomDto);
        int result = adminService.insertRoom(roomDto);
        if (result > 0) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "추가 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.BAD_REQUEST, "추가 실패"));
        }
    }

}
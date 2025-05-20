package com.capstone.meetingmap.board.service;

import com.capstone.meetingmap.board.entity.Board;
import com.capstone.meetingmap.board.entity.BoardFile;
import com.capstone.meetingmap.board.repository.BoardFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BoardFileService {
    private final BoardFileRepository boardFileRepository;

    public BoardFileService(BoardFileRepository boardFileRepository) {
        this.boardFileRepository = boardFileRepository;
    }

    @Transactional
    public List<BoardFile> saveFiles(Board board, List<MultipartFile> files) throws IOException {
        List<BoardFile> savedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            // 파일 저장 로직
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String fileUrl = "/uploads/" + fileName; // 서버 로컬 디스크에 업로드
            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            BoardFile boardFile = BoardFile.builder()
                    .fileName(file.getOriginalFilename())
                    .fileUrl(fileUrl)
                    .board(board)
                    .build();
            boardFileRepository.save(boardFile);
            savedFiles.add(boardFile);
        }
        return savedFiles;
    }

    @Transactional
    public void deleteFiles(Integer boardNo) {
        List<BoardFile> files = boardFileRepository.findAllByBoard_BoardNo(boardNo);
        boardFileRepository.deleteAll(files);
    }
}

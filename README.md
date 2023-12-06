# Board v1.7.0

## 개발환경
IDE: IntelliJ IDEA Community  
Spring Boot 2.7.6  
JDK 11  
mysql 8.0.35  
Lombok  
Spring Data JPA
Spring Web   
Thymeleaf  
aplication.yml

## 게시판 주요 기능

### 게시글
1. 게시글 작성(/board/save)
-파일(이미지) 첨부

2. 게시글 목록(/board/,/board/paging)  
\- 한 페이지 게시글 5개  
\- 최대 3개 페이지씩  
\- /board/paging?page=1  
\- /board/paging/1

3. 게시글 조회(/board/{id})

4. 게시글 수정(/board/update/{id})  
\- 상세화면에서 수정 버튼 클릭    
\- 서버에서 해당 게시글의 정보를 가지고 수정 화면 출력    
\- 제목, 내용 수정 입력 받아서 서버로 요청  

5. 게시글 삭제(/board/delete/{id})  
\- 상세화면에서 삭제 버튼 클릭  
\- 삭제  ? 으로
---

### 댓글
1. 댓글 작성(/comment/save)
\- 작성자와 내용 입력 후 댓글 작성
2. 댓글 목록(/list/{boardId})  
\- 해당 게시글 id에 달린 댓글 리스트 보이기
3. 댓글 수정(/update/{commentId})  
\- 댓글 수정 버튼 클릭 후 수정 내용 작성  
\-
\- 확인 버튼 클릭  
\- 수정 성공시 창 알림  
4. 댓글 삭제(/delete/{commentId)

---
### 파일
1. 파일 다운(/download/{uuid}/{fileName})
2. 파일 수정
3. 파일 삭제(file/delete/{boardId})

## 향후 업데이트 예정 기능
1. 다중 파일 첨부 기능

#### v1.0.0 (2023.11.21)
1. [추가] 게시판 생성(/board/save)

#### v1.1.0 (2023.11.22)
1. [추가] 게시글 페이징(/board/paging, /board/)  
\- 한 페이지 5개씩  
\- 페이지 최대 3개씩

#### v1.2.0 (2023.11.23)
1. [추가] 게시글 U
2. [추가] 게시글 D

#### v1.3.0 (2023.11.24)
1. [추가] 댓글 생성(/comment/save)

#### v1.4.0 (2023.11.27)
1. [추가] 게시글 파일 업로드

#### v1.5.0 (2023.11.28)
1. [추가] 게시글 파일 다운로드(/download/{uuid}/{fileName})

#### v1.6.0 (2023.11.29)
1. [추가] 댓글 수정(/update/{commentId})
2. [추가] 댓글 삭제(/delete/{commentId})

#### v1.7.0 (2023.12.01)
1. [추가] 파일 삭제(file/delete/{boardId})

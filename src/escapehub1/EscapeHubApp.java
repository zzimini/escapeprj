package escapehub1;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EscapeHubApp {

    static Scanner sc = new Scanner(System.in);
    static String currentUserId = null; // 로그인한 사용자 ID 저장


    public static void main(String[] args) {
    	
    	//TimeSlotManager.insertSampleTimeSlotsForFullYear("2025-04-20", 365);

        boolean run = true;

        while (run) {
            System.out.println("\n======= EscapeHub 서버 =======");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 종료");
            System.out.print("선택 > ");
            System.out.println("git update 연습");
            

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    System.out.println("EscapeHub 서버를 종료합니다.");
                    run = false;
                }
                default -> System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    private static void login() {
        System.out.print("아이디: ");
        String id = sc.nextLine();
        System.out.print("비밀번호: ");
        String pw = sc.nextLine();

        User loginUser = UserDAO.login(id, pw);

        if (loginUser != null) {
            currentUserId = loginUser.getUserId();
            System.out.println("\n" + currentUserId + "님 환영합니다!");

            if (loginUser.isAdmin()) {
                adminMenu();
            } else {
                userMenu();
            }
        } else {
            System.out.println("❌ 로그인 실패! 아이디 또는 비밀번호를 다시 확인하세요.");
        }
    }


    private static void register() {
        System.out.print("새 아이디: ");
        String id = sc.nextLine();

        System.out.print("새 비밀번호: ");
        String pw = sc.nextLine();

        boolean isAdmin = false;
        if (id.equalsIgnoreCase("admin")) {
            isAdmin = true;
        }

        User newUser = new User(id, pw, isAdmin);

        if (UserDAO.registerUser(newUser)) {
            System.out.println("✅ 회원가입이 완료되었습니다!");
        } else {
            System.out.println("❌ 회원가입 실패 (아이디 중복 또는 오류)");
        }
    }

    
    private static void userMenu() {
        boolean run = true;
        while (run) {
            System.out.println("\n======= 사용자 메뉴 =======");
            System.out.println("1. 방탈출 전체 조회/예약");
            System.out.println("2. MY 예약 조회");
            System.out.println("3. 리뷰 및 평점 등록 ");
            System.out.println("4. 로그아웃");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
            case 1 -> escapeRoomSubMenuuser(); // **서브메뉴 호출**
            case 2 -> ReservationManager.myReservations(currentUserId);
            case 3 -> writeReviewFlow(currentUserId);
            case 4 -> {
                System.out.println("로그아웃합니다.");
                run = false;
            }
            default -> System.out.println("올바른 메뉴를 선택하세요.");
        }
    }
}
    
 // 방탈출 조회 서브메뉴
    private static void escapeRoomSubMenuuser() {
        boolean run = true;
        while (run) {
            System.out.println("\n===== 방탈출 조회/예약 메뉴 =====");
            System.out.println("1. 전체 목록 조회");
            System.out.println("2. 지역별 조회");
            System.out.println("3. 테마별 조회");
            System.out.println("4. 난이도별 조회");
            System.out.println("5. 인기순 조회");
            System.out.println("6. 뒤로가기");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
                case 1 -> EscapeRoomManager.listAllRoomsuser();
                case 2 -> EscapeRoomManager.listByLocationuser();
                case 3 -> EscapeRoomManager.listByThemeuser();
                case 4 -> EscapeRoomManager.listByDifficultyuser();
                case 5 -> EscapeRoomManager.listByPopularityuser();
                case 6 -> {
                    System.out.println("사용자 메뉴로 돌아갑니다.");
                    run = false;
                }
                default -> System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    private static void adminMenu() {
        boolean run = true;
        while (run) {
            System.out.println("\n===== 관리자 메뉴 =====");
            System.out.println("1. 방탈출 전체 조회");
            System.out.println("2. 방탈출 신규 등록");
            System.out.println("3. 방탈출 수정/삭제");
            System.out.println("4. 회원 목록 조회/관리");
            System.out.println("5. 예약 전체 조회/관리");
            System.out.println("6. 리뷰 목록 조회/관리");
            System.out.println("7. 테마 예약 시간 관리");
            System.out.println("8. 로그아웃");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
            case 1 -> escapeRoomSubMenuAdmin(); // **서브메뉴 호출**
            case 2 -> EscapeRoomManager.registerRoom();
            case 3 -> roomEditSubMenu();
            case 4 -> manageUsers();
            case 5 -> manageAllReservations();
            case 6 -> manageReviews();
            case 7 -> manageTimeSlots();
            case 8 -> {
                System.out.println("로그아웃합니다.");
                run = false;
            }
            default -> System.out.println("올바른 메뉴를 선택하세요.");
        }
    }
}
    
 // 관리자용 방탈출 조회 서브메뉴
    private static void escapeRoomSubMenuAdmin() {
        boolean run = true;
        while (run) {
            System.out.println("\n===== 관리자 방탈출 조회 메뉴 =====");
            System.out.println("1. 전체 목록 조회");
            System.out.println("2. 지역별 조회");
            System.out.println("3. 테마별 조회");
            System.out.println("4. 난이도별 조회");
            System.out.println("5. 인기순 조회");
            System.out.println("6. 뒤로가기");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
                case 1 -> EscapeRoomManager.listAllRooms();
                case 2 -> EscapeRoomManager.listByLocation();
                case 3 -> EscapeRoomManager.listByTheme();
                case 4 -> EscapeRoomManager.listByDifficulty();
                case 5 -> EscapeRoomManager.listByPopularity();
                case 6 -> {
                    System.out.println("관리자 메뉴로 돌아갑니다.");
                    run = false;
                }
                default -> System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }
    //방수정, 삭제
    private static void roomEditSubMenu() {
        boolean run = true;
        while (run) {
            System.out.println("\n===== 방 수정/삭제 메뉴 =====");
            System.out.println("1. 방 정보 수정");
            System.out.println("2. 방 정보 삭제");
            System.out.println("3. 뒤로가기");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
                case 1 -> EscapeRoomManager.updateRoom();
                case 2 -> EscapeRoomManager.deleteRoom();
                case 3 -> {
                    System.out.println("관리자 메뉴로 돌아갑니다.");
                    run = false;
                }
                default -> System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }
    
    private static void manageUsers() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 회원 목록 조회/관리 =====");
            List<User> userList = UserDAO.getAllUsers();

            if (userList.isEmpty()) {
                System.out.println("📭 등록된 회원이 없습니다.");
            } else {
                for (User u : userList) {
                    System.out.println("- ID: " + u.getUserId() + " | Password: " + u.getPassword()+ " | 관리자: " + (u.isAdmin() ? "O" : "X"));
                }
            }

            System.out.println("\n1. 회원 삭제");
            System.out.println("2. 뒤로가기");
            System.out.print("선택 > ");
            String input = sc.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.print("삭제할 회원 ID 입력 > ");
                    String deleteId = sc.nextLine();

                    if (deleteId.equalsIgnoreCase("admin")) {
                        System.out.println("❌ admin 계정은 삭제할 수 없습니다.");
                    } else if (UserDAO.deleteUser(deleteId)) {
                        System.out.println("✅ 회원이 삭제되었습니다.");
                    } else {
                        System.out.println("❌ 삭제 실패 (존재하지 않거나 오류 발생)");
                    }
                }
                case "2" -> {
                    System.out.println("관리자 메뉴로 돌아갑니다.");
                    run = false;
                }
                default -> System.out.println("❌ 올바른 번호를 입력하세요.");
            }
        }
    }

    
    //리뷰 클래스
 // 사용자 리뷰 작성 플로우
    private static void writeReviewFlow(String userId) {
        List<EscapeRoom> reservedRooms = ReservationManager.getReservedRoomsByUser(userId);

        if (reservedRooms.isEmpty()) {
            System.out.println("❌ 리뷰를 작성할 수 있는 예약 내역이 없습니다.");
            return;
        }

        System.out.println("\n===== 리뷰 작성 가능 방 목록 =====");
        for (EscapeRoom room : reservedRooms) {
            System.out.println("[" + room.getRoomId() + "] " + room.getName() + " (" +
                    room.getBranchName() + ", " + room.getLocation() + ", " +
                    room.getTheme() + ", " + room.getDifficulty() + ", 평점: " + room.getRating() + ")");
        }

        System.out.print("\n리뷰를 작성할 방 ID 입력 (뒤로가기: 9) > ");
        String input = sc.nextLine().trim();

        if (input.equals("9")) return;

        try {
            int roomId = Integer.parseInt(input);
            boolean valid = reservedRooms.stream().anyMatch(room -> room.getRoomId() == roomId);

            if (!valid) {
                System.out.println("❌ 해당 방은 예약한 적이 없습니다.");
                return;
            }

            System.out.println("\n===== 리뷰 작성 =====");
            System.out.print("리뷰 내용을 입력하세요 > ");
            String content = sc.nextLine();

            double rating = -1;
            while (rating < 1.0 || rating > 5.0) {
                System.out.print("평점을 입력하세요 (1.0 ~ 5.0) > ");
                try {
                    rating = Double.parseDouble(sc.nextLine());
                    if (rating < 1.0 || rating > 5.0) {
                        System.out.println("❌ 유효한 범위(1.0~5.0)로 입력해주세요.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ 숫자 형식으로 입력해주세요.");
                }
            }

            // insert + 평균 반영
            ReviewManager.writeReview(userId, roomId, content, rating);

        } catch (NumberFormatException e) {
            System.out.println("❌ 숫자를 입력해주세요.");
        }
    }
    
    private static void manageAllReservations() {
        boolean run = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (run) {
            System.out.println("\n===== 예약 목록 관리 =====");
            System.out.println("1. 전체 예약 조회");
            System.out.println("2. 특정 방 예약 조회");
            System.out.println("3. 예약 삭제");
            System.out.println("4. 뒤로가기");
            System.out.print("선택 > ");
            String input = sc.nextLine();

            switch (input) {
                case "1" -> {
                    List<Reservation> all = ReservationDAO.getAllReservations();
                    if (all.isEmpty()) {
                        System.out.println("📭 등록된 예약이 없습니다.");
                    } else {
                        System.out.println("\n===== 전체 예약 목록 =====");
                        for (Reservation r : all) {
                            EscapeRoom room = EscapeRoomDAO.findRoomById(r.getRoomId());
                            String roomName = (room != null) ? room.getName() : "(알 수 없음)";

                            System.out.printf("[예약ID: %d] 사용자: %s | 방: %s (ID: %d) | 시간: %s\n",
                                    r.getReservationId(), r.getUserId(), roomName, r.getRoomId(),
                                    r.getReserveTime().format(formatter));
                        }
                    }
                }

                case "2" -> {
                    System.out.print("조회할 방 ID 입력 > ");
                    String roomIdInput = sc.nextLine();
                    int roomId;
                    try {
                        roomId = Integer.parseInt(roomIdInput);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 방 ID는 숫자로 입력해주세요.");
                        break;
                    }

                    List<Reservation> filtered = ReservationDAO.getReservationsByRoomId(roomId);
                    if (filtered.isEmpty()) {
                        System.out.println("📭 해당 방의 예약이 없습니다.");
                    } else {
                        System.out.printf("\n===== 방 ID %d의 예약 목록 =====\n", roomId);
                        for (Reservation r : filtered) {
                        	EscapeRoom room = EscapeRoomDAO.findRoomById(r.getRoomId());
                            String roomName = (room != null) ? room.getName() : "(알 수 없음)";

                            System.out.printf("[예약ID: %d] 사용자: %s | 방: %s (ID: %d) | 시간: %s\n",
                                    r.getReservationId(), r.getUserId(), roomName, r.getRoomId(),
                                    r.getReserveTime().format(formatter));
                        }
                    }
                }

                case "3" -> {
                    System.out.print("삭제할 예약번호 입력 > ");
                    String idInput = sc.nextLine().trim();
                    int delId;

                    try {
                        delId = Integer.parseInt(idInput);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 삭제할 예약번호가 없거나 잘못된 형식입니다.");
                        break;
                    }

                    Reservation r = ReservationDAO.findReservationById(delId);
                    if (r == null) {
                        System.out.println("❌ 삭제할 예약번호가 없거나 잘못된 형식입니다.");
                        break;
                    }
                    
                    EscapeRoom room = EscapeRoomDAO.findRoomById(r.getRoomId());
                    String roomName = (room != null) ? room.getName() : "(알 수 없음)";

                    System.out.printf("- 예약자: %s | 방: %s (ID: %d) | 시간: %s\n",
                            r.getUserId(), roomName, r.getRoomId(),
                            r.getReserveTime().format(formatter));

                    System.out.print("정말 삭제하시겠습니까? (Y/N): ");
                    String confirm = sc.nextLine().trim();
                    if (!confirm.equalsIgnoreCase("Y")) {
                        System.out.println("❌ 삭제가 취소되었습니다.");
                        break;
                    }

                    if (ReservationDAO.deleteReservationByAdmin(delId)) {
                        System.out.println("✅ 예약이 삭제되었습니다.");
                    } else {
                        System.out.println("❌ 삭제 실패 (예약번호 확인)");
                    }
                }

                case "4" -> run = false;

                default -> System.out.println("❌ 올바른 번호를 입력하세요.");
            }
        }
    }

    private static void manageReviews() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 리뷰 목록 관리 =====");
            System.out.println("1. 전체 리뷰 조회");
            System.out.println("2. 특정 방 리뷰 조회");
            System.out.println("3. 리뷰 삭제");
            System.out.println("4. 뒤로가기");
            System.out.print("선택 > ");

            String input = sc.nextLine();

            switch (input) {
                case "1" -> ReviewManager.viewAllReviewsForAdmin();
                case "2" -> {
                    System.out.print("방 ID 입력 > ");
                    try {
                        int roomId = Integer.parseInt(sc.nextLine());
                        ReviewManager.viewReviewsByRoomId(roomId);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ 숫자로 입력해주세요.");
                    }
                }
                case "3" -> {
                    System.out.print("삭제할 리뷰 ID 입력 > ");
                    try {
                        int reviewId = Integer.parseInt(sc.nextLine());

                        // 리뷰 정보 조회
                        Review review = ReviewManager.getReviewById(reviewId);
                        if (review == null) {
                            System.out.println("❌ 삭제 실패 : 등록되지 않은 리뷰입니다.");
                            break;
                        }

                        // 방 정보 출력
                        EscapeRoom room = EscapeRoomDAO.findRoomById(review.getRoomId());
                        String roomInfo = (room != null) ? room.getName() + " (ID: " + review.getRoomId() + ")" : "(방 정보 없음)";

                        // 리뷰 내용 미리 출력
                        System.out.printf("- 사용자: %s | 방: %s | 평점: %.1f\n- 내용: %s\n",
                                review.getUserId(), roomInfo, review.getRating(), review.getContent());

                        // 삭제 여부 확인
                        System.out.print("정말 삭제하시겠습니까? (Y/N): ");
                        String confirm = sc.nextLine().trim();
                        if (!confirm.equalsIgnoreCase("Y")) {
                            System.out.println("❌ 삭제가 취소되었습니다.");
                            break;
                        }

                        boolean deleted = ReviewManager.deleteReviewByAdmin(reviewId);
                        System.out.println(deleted ? "✅ 삭제되었습니다." : "❌ 삭제 실패");

                    } catch (NumberFormatException e) {
                        System.out.println("❌ 숫자로 입력해주세요.");
                    }
                }


                case "4" -> run = false;
                default -> System.out.println("❌ 올바른 번호를 입력하세요.");
            }
        }
    }
    private static void manageTimeSlots() {
        System.out.println("\n===== 예약 시간 관리 =====");

        try {
            System.out.print("방 ID 입력 > ");
            int roomId = Integer.parseInt(sc.nextLine());

            EscapeRoom room = EscapeRoomDAO.findRoomById(roomId);
            if (room == null) {
                System.out.println("❌ 존재하지 않는 방 ID입니다.");
                return;
            }

            // 현재 등록된 시간 출력
            List<String> allTimes = TimeSlotManager.getAvailableTimesForRoom(roomId);
            if (allTimes.isEmpty()) {
                System.out.println("📭 현재 예약 가능한 시간이 없습니다.");
            } else {
                System.out.println("📌 현재 예약 가능한 시간:");
                for (String t : allTimes) System.out.println("- " + t);
            }

            System.out.println("\n※ 아래 항목 중 입력하지 않으면 해당 작업은 건너뜁니다. Enter를 눌러 건너뛰어 주세요.");

            // 1. 추가
            System.out.print("▶ 예약시간 추가 (예: 12:00,14:00,16:30) > ");
            String addInput = sc.nextLine().trim();
            if (!addInput.isEmpty()) {
                String[] addTimes = addInput.split(",");
                boolean allAdded = true;

                for (String t : addTimes) {
                    if (!TimeSlotManager.insertTimeForAllDates(roomId, t.trim())) {
                        System.out.println("❌ 추가 실패: " + t.trim());
                        allAdded = false;
                    }
                }

                if (allAdded) {
                    System.out.println("✅ 모든 시간 추가 완료");
                }
            }

         // 2. 변경
            System.out.print("▶ 예약시간 변경 (예: 12:00>13:30, 18:20>18:30) > ");
            String change = sc.nextLine().trim();
            if (!change.isEmpty()) {
                List<String[]> timePairs = new ArrayList<>();
                String[] pairs = change.split(",");
                boolean valid = true;

                for (String pair : pairs) {
                    String[] times = pair.trim().split(">");
                    if (times.length == 2) {
                        timePairs.add(new String[]{times[0].trim(), times[1].trim()});
                    } else {
                        valid = false;
                        break;
                    }
                }

                if (!valid) {
                    System.out.println("❌ 입력 형식이 잘못되었습니다. 예: 12:00>13:30, 18:20>18:30");
                } else {
                    boolean updated = TimeSlotManager.updateMultipleTimesForAllDates(roomId, timePairs);
                    System.out.println(updated ? "✅ 시간 변경 완료" : "❌ 변경 실패 (기존 시간이 없거나 중복)");
                }
            }


            // 3. 삭제
            System.out.print("▶ 예약시간 삭제 (예: 15:10,16:30) > ");
            String delete = sc.nextLine().trim();
            if (!delete.isEmpty()) {
                boolean deleted = TimeSlotManager.deleteTimeForAllDates(roomId, delete);
                System.out.println(deleted ? "✅ 시간 삭제 완료" : "❌ 삭제 실패 (해당 시간이 존재하지 않음)");
            }
            TimeSlotInitializer.generateTimeSlotsFromAdminSetting();
            System.out.println("📌 입력하신 시간들이 모든 날짜에 반영되었습니다.");

            System.out.println("📝 작업이 완료되었습니다.");

        } catch (NumberFormatException e) {
            System.out.println("❌ 방 ID는 숫자로 입력해주세요.");
        } catch (Exception e) {
            System.out.println("❌ 예기치 못한 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }



}
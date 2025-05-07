package escapehub1;

import java.util.List;
import java.util.Scanner;

public class EscapeRoomManager {

    private static Scanner sc = new Scanner(System.in);

    // 방탈출 등록 (관리자 기능)
    public static void registerRoom() {
        System.out.println("\n===== 방탈출 신규 등록 =====");

        try {
            System.out.print("방 ID (숫자입력): ");
            int id = Integer.parseInt(sc.nextLine().trim());
            
            if(EscapeRoomDAO.findRoomById(id)!=null) {
            	System.out.println("❌ 잘못된 형식이거나 이미 등록된 ID입니다.");
            	return;
            }

            System.out.print("방 이름: ");
            String name = sc.nextLine();

            System.out.print("지점 이름: ");
            String branchName = sc.nextLine();

            System.out.print("지역(홍대/강남/건대/대학로/신촌/잠실/신림): ");
            String location = sc.nextLine();

            System.out.print("테마(공포/감성/추리/액션/코믹/기타): ");
            String theme = sc.nextLine();

            System.out.print("난이도(입문/초급/중급/고급): ");
            String difficulty = sc.nextLine();

            EscapeRoom newRoom = new EscapeRoom(id, name, branchName, location, theme, difficulty, 0.0);

            boolean inserted = EscapeRoomDAO.insertRoom(newRoom);

            if (inserted) {
                System.out.println("✅ 방탈출이 성공적으로 등록되었습니다!");
            } else {
                System.out.println("❌ 등록실패: 잘못된 형식입니다.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ 숫자 형식이 잘못되었습니다.");
        } catch (Exception e) {
            // SQLException, NullPointer 등
            // e.printStackTrace(); ← 생략!
            System.out.println("❌ 잘못된 형식이거나 이미 등록된 ID입니다.");
        }
    }

//-------------------------------------------------어드민 서브------------------------------------------------------------
    // 전체 방 목록 조회 (Admin 기능)
    public static void listAllRooms() {
        System.out.println("\n===== 전체 방탈출 목록 =====");

        List<EscapeRoom> rooms = EscapeRoomDAO.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("등록된 방탈출이 없습니다.");
            return;
        }

        for (EscapeRoom room : rooms) {
            System.out.println(formatRoom(room));
        }
    }

    // 지역별 조회
    public static void listByLocation() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 지역별 조회 =====");
            System.out.println("1. 서울");
            System.out.println("2. 경기");
            System.out.println("3. 인천");
            System.out.println("4. 부산");
            System.out.println("5. 대구");
            System.out.println("6. 뒤로가기");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
                case 1 -> searchSeoulArea();
                case 2, 3, 4, 5 -> {
                    System.out.println("⚡ 해당 지역은 추후 구현 예정입니다.");
                }
                case 6 -> run = false;
                default -> System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }

    // 서울 지역 세부 검색
    private static void searchSeoulArea() {
        System.out.println("\n서울 지역 세부 선택");
        System.out.println("홍대/강남/건대/대학로/신촌/잠실/신림 중 선택하세요");
        System.out.print("입력 > ");
        String input = sc.nextLine();

        List<String> validSeoulAreas = List.of("홍대", "강남", "건대", "대학로", "신촌", "잠실", "신림");

        if (!validSeoulAreas.contains(input)) {
            System.out.println("⚠️ 유효하지 않은 지역입니다. 서울 지역 중에서 선택해주세요.");
            return;
        }

        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByLocation(input);
        if (rooms.isEmpty()) {
            System.out.println("해당 지역에 등록된 방탈출이 없습니다.");
        } else {
            for (EscapeRoom room : rooms) {
                System.out.println(formatRoom(room));
            }
        }
    }

    // 테마별 조회
    public static void listByTheme() {
        System.out.print("조회할 테마 입력(공포/감성/추리/액션/코믹/기타): ");
        String theme = sc.nextLine();

        System.out.println("\n===== " + theme + " 테마 방탈출 목록 =====");
        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByTheme(theme);

        if (rooms.isEmpty()) {
            System.out.println("해당 테마 방탈출이 없습니다.");
        } else {
            for (EscapeRoom room : rooms) {
                System.out.println(formatRoom(room));
            }
        }
    }

    // 난이도별 조회
    public static void listByDifficulty() {
        System.out.print("조회할 난이도 입력(입문/초급/중급/고급): ");
        String diff = sc.nextLine();

        System.out.println("\n===== " + diff + " 난이도 방탈출 목록 =====");
        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByDifficulty(diff);

        if (rooms.isEmpty()) {
            System.out.println("해당 난이도 방탈출이 없습니다.");
        } else {
            for (EscapeRoom room : rooms) {
                System.out.println(formatRoom(room));
            }
        }
    }

    // 인기순 조회 (별점 높은 순 정렬)
    public static void listByPopularity() {
        System.out.println("\n===== 인기순(별점 높은 순) 방탈출 목록 =====");

        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByPopularity();

        if (rooms.isEmpty()) {
            System.out.println("등록된 방탈출이 없습니다.");
        } else {
            for (EscapeRoom room : rooms) {
                System.out.println(formatRoom(room));
            }
        }
    }
    
    private static String formatRoom(EscapeRoom room) {
        return //"[" + room.getRoomId() + "] " +
               room.getName() + " (" +
               room.getBranchName() + ", " +
               room.getLocation() + ", " +
               room.getTheme() + ", " +
               room.getDifficulty() + ", 평점: " +
               room.getRating() + ")";
    }
  //-------------------------------------------------어드민 서브------------------------------------------------------------
  //------------------------------------------------유저 서브-------------------------------------------------------------------
    
    public static void listAllRoomsuser() {
        System.out.println("\n===== 전체 방탈출 목록 =====");

        List<EscapeRoom> rooms = EscapeRoomDAO.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("등록된 방탈출이 없습니다.");
            return;
        }

//        for (EscapeRoom room : rooms) {
//            System.out.println(formatRoomuser(room));
//        }
        
        handleUserRoomSelection(rooms, EscapeHubApp.currentUserId);
    }

    // 지역별 조회
    public static void listByLocationuser() {
        boolean run = true;

        while (run) {
            System.out.println("\n===== 지역별 조회 =====");
            System.out.println("1. 서울");
            System.out.println("2. 경기");
            System.out.println("3. 인천");
            System.out.println("4. 부산");
            System.out.println("5. 대구");
            System.out.println("6. 뒤로가기");
            System.out.print("선택 > ");

            int choice = sc.nextInt();
            sc.nextLine(); // 엔터 제거

            switch (choice) {
                case 1 -> searchSeoulAreauser();
                case 2, 3, 4, 5 -> {
                    System.out.println("⚡ 해당 지역은 추후 구현 예정입니다.");
                }
                case 6 -> run = false;
                default -> System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }

    // 서울 지역 세부 검색
    private static void searchSeoulAreauser() {
        System.out.println("\n서울 지역 세부 선택");
        System.out.println("홍대/강남/건대/대학로/신촌/잠실/신림 중 선택하세요");
        System.out.print("입력 > ");
        String input = sc.nextLine();

        List<String> validSeoulAreas = List.of("홍대", "강남", "건대", "대학로", "신촌", "잠실", "신림");

        if (!validSeoulAreas.contains(input)) {
            System.out.println("⚠️ 유효하지 않은 지역입니다. 서울 지역 중에서 선택해주세요.");
            return;
        }

        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByLocation(input);
        if (rooms.isEmpty()) {
            System.out.println("해당 지역에 등록된 방탈출이 없습니다.");
        } //else {
//            for (EscapeRoom room : rooms) {
//                System.out.println(formatRoomuser(room));
//            }
        //}
        
        handleUserRoomSelection(rooms, EscapeHubApp.currentUserId);
    }

    // 테마별 조회
    public static void listByThemeuser() {
        System.out.print("조회할 테마 입력(공포/감성/추리/액션/코믹/기타): ");
        String theme = sc.nextLine();

        System.out.println("\n===== " + theme + " 테마 방탈출 목록 =====");
        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByTheme(theme);

        if (rooms.isEmpty()) {
            System.out.println("해당 테마 방탈출이 없습니다.");
        } //else {
//            for (EscapeRoom room : rooms) {
//                System.out.println(formatRoomuser(room));
//            }
//        }
            handleUserRoomSelection(rooms, EscapeHubApp.currentUserId);
    }

    // 난이도별 조회
    public static void listByDifficultyuser() {
        System.out.print("조회할 난이도 입력(입문/초급/중급/고급): ");
        String diff = sc.nextLine();

        System.out.println("\n===== " + diff + " 난이도 방탈출 목록 =====");
        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByDifficulty(diff);

        if (rooms.isEmpty()) {
            System.out.println("해당 난이도 방탈출이 없습니다.");
        } //else {
//            for (EscapeRoom room : rooms) {
//                System.out.println(formatRoomuser(room));
//            }
//        }
        handleUserRoomSelection(rooms, EscapeHubApp.currentUserId);
    }

    // 인기순 조회 (별점 높은 순 정렬)
    public static void listByPopularityuser() {
        System.out.println("\n===== 인기순(별점 높은 순) 방탈출 목록 =====");

        List<EscapeRoom> rooms = EscapeRoomDAO.getRoomsByPopularity();

        if (rooms.isEmpty()) {
            System.out.println("등록된 방탈출이 없습니다.");
		} /*
			 * else { for (EscapeRoom room : rooms) {
			 * System.out.println(formatRoomuser(room)); } }
			 */
        handleUserRoomSelection(rooms, EscapeHubApp.currentUserId);
    }
    
    // 방 출력 포맷 통일 --일단 안씀
    private static String formatRoomuser(EscapeRoom room) {
        return //"[" + room.getRoomId() + "] " +
               room.getName() + " (" +
               room.getBranchName() + ", " +
               room.getLocation() + ", " +
               room.getTheme() + ", " +
               room.getDifficulty() + ", 평점: " +
               room.getRating() + ")" +
               "  (예약하기)";
    }
    
    //------------------------------------------------유저 서브-----------------------------------------------
 // 방 수정
    public static void updateRoom() {
        System.out.println("\n===== 방 수정 =====");
        System.out.print("수정할 방 ID 입력: ");
        int id = sc.nextInt();
        sc.nextLine(); // 엔터 제거

        EscapeRoom room = EscapeRoomDAO.findRoomById(id);
        if (room == null) {
            System.out.println("❌ 해당 ID의 방이 존재하지 않습니다.");
            return;
        }

        System.out.println("현재 방 정보:");
        System.out.println(formatRoom(room));

        System.out.println("※ 수정하지 않을 항목은 Enter만 누르세요.");

        System.out.print("새 방 이름 (현재: " + room.getName() + "): ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) {
            room.setName(newName);
        }

        System.out.print("새 지점 이름 (현재: " + room.getBranchName() + "): ");
        String newBranch = sc.nextLine();
        if (!newBranch.isEmpty()) {
            room.setBranchName(newBranch);
        }

        System.out.print("새 지역 (현재: " + room.getLocation() + "): ");
        String newLocation = sc.nextLine();
        if (!newLocation.isEmpty()) {
            room.setLocation(newLocation);
        }

        System.out.print("새 테마 (현재: " + room.getTheme() + "): ");
        String newTheme = sc.nextLine();
        if (!newTheme.isEmpty()) {
            room.setTheme(newTheme);
        }

        System.out.print("새 난이도 (현재: " + room.getDifficulty() + "): ");
        String newDifficulty = sc.nextLine();
        if (!newDifficulty.isEmpty()) {
            room.setDifficulty(newDifficulty);
        }

        if (EscapeRoomDAO.updateRoom(room)) {
            System.out.println("✅ 방 정보가 수정되었습니다!");
        } else {
            System.out.println("❌ 방 정보 수정 실패");
        }
    }

    // 방 삭제
    public static void deleteRoom() {
        System.out.println("\n===== 방 삭제 =====");
        System.out.print("삭제할 방 ID 입력: ");
        
        String input = sc.nextLine().trim();
        int id;

        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ 입력 형식이 잘못되었습니다. 숫자로 입력해주세요.");
            return;
        }

        EscapeRoom room = EscapeRoomDAO.findRoomById(id);
        if (room == null) {
            System.out.println("❌ 해당 ID의 방이 존재하지 않습니다.");
            return;
        }

        if (ReservationDAO.hasReservationsForRoom(id)) {
            System.out.println("❌ 해당 방은 예약건이 존재하여 삭제할 수 없습니다.");
            return;
        }

        System.out.println("삭제할 방 정보:");
        System.out.println(formatRoom(room));

        System.out.print("정말 삭제하시겠습니까? (Y/N): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            if (EscapeRoomDAO.deleteRoom(id)) {
                System.out.println("✅ 방이 삭제되었습니다!");
            } else {
                System.out.println("❌ 방 삭제 실패");
            }
        } else {
            System.out.println("삭제가 취소되었습니다.");
        }
    }

    //=================유저 조회/예약하기 핸들러 ======================
    public static void handleUserRoomSelection(List<EscapeRoom> rooms, String userId) {
        if (rooms.isEmpty()) {
            System.out.println("조회 결과가 없습니다.");
            return;
        }

        for (EscapeRoom room : rooms) {
            System.out.println("[" + room.getRoomId() + "] " + room.getName() +
                    " (" + room.getBranchName() + ", " + room.getLocation() + ", " +
                    room.getTheme() + ", " + room.getDifficulty() + ", 평점: " + room.getRating() + ") (예약하기)");
        }

        while (true) {
            System.out.println("\n예약하려면 [방ID,0] 입력, 리뷰 보려면 [방ID,1] 입력, 뒤로가려면 9 입력");
            System.out.print("선택 > ");
            String input = sc.nextLine().trim();

            if (input.equals("9")) break;

            try {
                String[] parts = input.split(",");
                int roomId = Integer.parseInt(parts[0]);
                int action = Integer.parseInt(parts[1]);

                EscapeRoom selectedRoom = EscapeRoomDAO.findRoomById(roomId);
                if (selectedRoom == null) {
                    System.out.println("❌ 해당 ID의 방이 존재하지 않습니다.");
                    continue;
                }

                switch (action) {
                    case 0 -> ReservationManager.startReservation(userId, roomId);
                    case 1 -> ReviewManager.viewReviews(roomId);
                    default -> System.out.println("❌ 잘못된 입력입니다.");
                }

            } catch (Exception e) {
                System.out.println("❌ 입력 형식이 잘못되었습니다.");
            }
        }
    }
    //================================================유저예약하기======================
    
    


}

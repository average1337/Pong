package playpong;

public class StartPong {

	public static void main(String[] args) {
		Graphics game = new Graphics();
		new Thread(game).start();
	}

}

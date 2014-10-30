package aki;

import aki.links.LinksGenerator;

public class UtilsStarter {
	public static void main(String[] args) {
		LinksGenerator linksGenerator = new LinksGenerator();
		for (int i = 0; i < 1000; i++) {
			linksGenerator.addLink();
		}
	}
}
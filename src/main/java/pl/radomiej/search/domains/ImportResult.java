package pl.radomiej.search.domains;

public class ImportResult {

	private int saveNodes;

	public void incrasePersistResultNode() {
		saveNodes++;
	}

	public int getSaveNodes() {
		return saveNodes;
	}

	public void setSaveNodes(int saveNodes) {
		this.saveNodes = saveNodes;
	}

}

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;

import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Coppia;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnCreaGrafo;

	@FXML
	private Button btnArtistiConnessi;

	@FXML
	private Button btnCalcolaPercorso;

	@FXML
	private ComboBox<String> boxRuolo;

	@FXML
	private TextField txtArtista;

	@FXML
	private TextArea txtResult;

	@FXML
	void doArtistiConnessi(ActionEvent event) {
		txtResult.clear();
		txtResult.appendText("Calcola artisti connessi \n");

		List<Coppia> coppie = this.model.getCoppie();
		txtResult.appendText("Gli artisti connessi sono: \n");
		for (Coppia c : coppie)
			txtResult.appendText(c.toString() + "\n");
		
		btnCalcolaPercorso.setDisable(false);

	}

	@FXML
	void doCalcolaPercorso(ActionEvent event) {
		txtResult.clear();
		txtResult.appendText("Calcola percorso \n");

		Integer artistaId;

		try {
			artistaId = Integer.parseInt(txtArtista.getText());
		} catch (NumberFormatException e) {
			txtResult.setText("L'ID dell'artista è formato solo da numeri");
			return;
		}

		// controlliamo che l'id si contenuto nel grafo
		if (!this.model.contiene(artistaId)) {
			txtResult.setText("L'artista non è nel grafo");
			return;
		}

		List<Artist> vertici = this.model.trovaPercorso(artistaId);
		txtResult.appendText(String.format("Percorso migliore con %d esposizioni condivise: \n\n",this.model.getBestPeso()));

		for (Artist s : vertici) {
			txtResult.appendText(s.toString() + "\n");
		}

	}

	@FXML
	void doCreaGrafo(ActionEvent event) {
		txtResult.clear();
		txtResult.appendText("Crea grafo");

		String ruolo = boxRuolo.getValue();
		if (ruolo == null) {
			txtResult.setText("Seleziona un ruolo");
			return;
		}

		this.model.creaGrafo(ruolo);
		btnArtistiConnessi.setDisable(false);

		txtResult.setText(String.format("Grafo creato con %d vertici e %d archi",
				this.model.getGrafo().vertexSet().size(), this.model.getGrafo().edgeSet().size()));

	}

	public void setModel(Model model) {
		this.model = model;

		boxRuolo.getItems().addAll(this.model.getRuoli());

		btnArtistiConnessi.setDisable(true);
		btnCalcolaPercorso.setDisable(true);

	}

	@FXML
	void initialize() {
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}
}

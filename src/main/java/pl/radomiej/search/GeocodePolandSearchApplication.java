package pl.radomiej.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.common.io.Files;

import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import pl.radomiej.search.services.ImporterService;
import pl.radomiej.search.services.PostalcodesFixService;

@SpringBootApplication(exclude = {ElasticsearchAutoConfiguration.class, ElasticsearchDataAutoConfiguration.class})
public class GeocodePolandSearchApplication implements CommandLineRunner{
	@Autowired
	private ImporterService importService;

	@Autowired
	private PostalcodesFixService postalcodesFixService;
	
	private boolean importCodik;
	
	public static void main(String[] args) {
		SpringApplication.run(GeocodePolandSearchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	
		if(args.length < 1 || !args[0].equals("import")){
			return;
		}
		
		List<String> files = new ArrayList<>();
		File root = new File(args[1]);
		for (File file : root.listFiles()) {
			if (file.isDirectory())
				continue;
			if (Files.getFileExtension(file.getName()).equalsIgnoreCase("xml")) {
				files.add(file.getAbsolutePath());
			}
		}

		for (String path : files) {
			System.out.println("Importing: " + path);
			importService.importAddresses(path);	
			System.out.println("Imported: " + path);
		}

		postalcodesFixService.fixPostalcodes();
	}
}

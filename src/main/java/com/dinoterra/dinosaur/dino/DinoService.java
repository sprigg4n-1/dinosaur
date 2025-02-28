package com.dinoterra.dinosaur.dino;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dinoterra.dinosaur.image.ImageRepository;
import com.dinoterra.dinosaur.image.ImageResponse;
import com.dinoterra.dinosaur.location.FoundLocationRepository;
import com.dinoterra.dinosaur.location.FoundLocationResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DinoService {
    private final DinoRepository dinoRepository;
    private final ImageRepository imageRepository;
    private final FoundLocationRepository locationRepository;

    public DinoResponse getDino(Long id) {
        Dino dino = dinoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dino not found with ID " + id));

        List<ImageResponse> images = imageRepository.findByDino(dino)
            .stream()
            .map(image -> new ImageResponse(image.getId(), image.getImage(), image.getFileName(), dino.getId()))
            .collect(Collectors.toList());

        
        List<FoundLocationResponse> locations = locationRepository.findByDino(dino)
            .stream()
            .map(location -> new FoundLocationResponse(location.getId(), location.getPlace(), location.getLatitude(), location.getLongitude(), dino.getId()))
            .collect(Collectors.toList());    

        return mapToDinoRes(dino, images, locations);
    }

    public List<DinoResponse> getAllDinos() {
        List<Dino> dinos = dinoRepository.findAll();
    
        return dinos.stream().map(dino -> {
            List<ImageResponse> images = imageRepository.findByDino(dino)
                .stream()
                .map(image -> new ImageResponse(image.getId(), image.getImage(), image.getFileName(), dino.getId()))
                .collect(Collectors.toList());
            List<FoundLocationResponse> locations = locationRepository.findByDino(dino)
                .stream()
                .map(location -> new FoundLocationResponse(location.getId(), location.getPlace(), location.getLatitude(), location.getLongitude(), dino.getId()))
                .collect(Collectors.toList());   
    
            return mapToDinoRes(dino, images, locations);
        }).collect(Collectors.toList());
    }

    public DinoResponse createDino(DinoRequest dinoRequest) {
        var dino = mapToDinoReq(dinoRequest);
        dinoRepository.save(dino);

        return mapToDinoRes(dino, null, null);
    }

    public void changeDino(Long id, DinoRequest dinoRequest) {
        Dino dino = dinoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dino not found with ID " + id));

        dino.setName(dinoRequest.name());
        dino.setLatinName(dinoRequest.latinName());
        dino.setDescription(dinoRequest.description());
        dino.setTypeOfDino(dinoRequest.typeOfDino());
        dino.setLength(dinoRequest.length());
        dino.setWeight(dinoRequest.weight());
        dino.setPeriod(dinoRequest.period());
        dino.setPeriodDate(dinoRequest.periodDate());
        dino.setPeriodDescription(dinoRequest.periodDescription());
        dino.setDiet(dinoRequest.diet());
        dino.setDietDescription(dinoRequest.dietDescription());

        dinoRepository.save(dino);
    }

    public void deleteDino(Long id) {
        dinoRepository.deleteById(id);
    }

    private Dino mapToDinoReq(DinoRequest dinoRequest) {
        Dino dino = new Dino();
        dino.setName(dinoRequest.name());
        dino.setLatinName(dinoRequest.latinName());
        dino.setDescription(dinoRequest.description());
        dino.setTypeOfDino(dinoRequest.typeOfDino());
        dino.setLength(dinoRequest.length());
        dino.setWeight(dinoRequest.weight());
        dino.setPeriod(dinoRequest.period());
        dino.setPeriodDate(dinoRequest.periodDate());
        dino.setPeriodDescription(dinoRequest.periodDescription());
        dino.setDiet(dinoRequest.diet());
        dino.setDietDescription(dinoRequest.dietDescription());
        return dino;
    }

    private DinoResponse mapToDinoRes(Dino dino, List<ImageResponse> images, List<FoundLocationResponse> locations) {
        return new DinoResponse(dino.getId(), dino.getName(), dino.getLatinName(), dino.getDescription(), dino.getTypeOfDino(), dino.getLength(), dino.getWeight(), dino.getPeriod(), dino.getPeriodDate(),
                dino.getPeriodDescription(), dino.getDiet(),  dino.getDietDescription(), images, locations);
    }
}

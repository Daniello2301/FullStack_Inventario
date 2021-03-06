package co.iudigital.backend_inventario.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.iudigital.backend_inventario.dto.EquipoDto;
import co.iudigital.backend_inventario.exception.BadRequestException;
import co.iudigital.backend_inventario.exception.ErrorDto;
import co.iudigital.backend_inventario.exception.InternalServerErrorException;
import co.iudigital.backend_inventario.exception.NotFoundException;
import co.iudigital.backend_inventario.exception.RestException;
import co.iudigital.backend_inventario.model.Equipo;
import co.iudigital.backend_inventario.service.iface.IEquipoService;

@RestController
@RequestMapping("/equipos")
@CrossOrigin("*")
public class EquipoController {
    
    private static final Logger LOG = LoggerFactory.getLogger(EquipoController.class);

    @Autowired
    private IEquipoService equipoService;


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAll() throws RestException
    {
        LOG.info("Equipos...");
        List<EquipoDto> response = 
                equipoService.getAll();
        
        return ResponseEntity.ok().body(response);
    }



    
    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> getById(@PathVariable Long id) throws RestException
    {
        EquipoDto response =
                equipoService.getById(id);
        
        return ResponseEntity.ok().body(response);
    }




    @GetMapping("/pagination/{pageNumber}/{pageSize}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getEquiposPagination(@PathVariable int pageNumber, @PathVariable int pageSize) throws RestException
    {
        LOG.info("Equipos...");
        Page<Equipo> response = 
                equipoService.getEquiposPagination(pageNumber, pageSize);
        
        return ResponseEntity.ok().body(response);
    }




    @GetMapping("/sortby/{field}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getSortBy(@PathVariable String field) throws RestException
    {
        LOG.info("Equipos...");
        List<EquipoDto> response = 
                equipoService.getSortBy(field);
        
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/paginationAndSort/{pageNumber}/{pageSize}/{field}")
    public ResponseEntity<Object> getEquiposPaginationAndSorting(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable String field) throws RestException{

        LOG.info("Pagination And Sort Equipos");
        Page<Equipo> response = equipoService.getEquiposPaginaionAndSorting(pageNumber, pageSize, field);

        return ResponseEntity.ok().body(response);


    }




    @PostMapping(consumes = "application/json")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<EquipoDto> save(@RequestBody EquipoDto equipoDto) throws RestException
    {
        try 
        {
            EquipoDto response = equipoService.save(equipoDto);
            
            return new ResponseEntity<>(response,
                                        HttpStatus.CREATED
                                        );

        } 
        catch (BadRequestException e) 
        {
            LOG.error("Error", e);
            throw e;
        } 
        catch(Exception e)
        {
            throw new InternalServerErrorException(ErrorDto
                        .getErrorDto(
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
								"Error de backend", 
                            HttpStatus.INTERNAL_SERVER_ERROR.value()
                            )
					);
        }
    }



    @PutMapping(consumes = "application/json")
    @ResponseStatus
    public ResponseEntity<EquipoDto> update(@RequestBody EquipoDto equipoDto) throws RestException
    {
        try 
        {
            EquipoDto response = equipoService.update(equipoDto);
         
         return new ResponseEntity<>(response, HttpStatus.OK);
          
        } catch (BadRequestException e) 
        {
            LOG.error("Error", e);
            throw e; 
        } catch (InternalServerErrorException e)
        {
            throw new InternalServerErrorException(
                ErrorDto.getErrorDto(
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), 
                    "Error interno en el servidor",
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
                    )
            ); 
        }
    }





    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) throws RestException
    {
        EquipoDto equipo = equipoService.getById(id);
        if(equipo == null)
        {
            throw new NotFoundException(ErrorDto
                .getErrorDto(
                    HttpStatus.NOT_FOUND.getReasonPhrase(), 
                    "No se encuentra", 
                    HttpStatus.NOT_FOUND.value()
                    )
                ); 
        }
        else
        {
            equipoService.deleteById(id);
        }
    }
}
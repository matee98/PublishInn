package com.github.PublishInn.service;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkInfoDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.dto.mappers.WorkMapper;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.exceptions.WorkException;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import com.github.PublishInn.model.repository.UserRepository;
import com.github.PublishInn.model.repository.WorkRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkService {
    private final WorkRepository workRepository;
    private final UserRepository userRepository;

    public void saveWork (WorkSaveDto model, Principal principal) throws UserException {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Work work = mapper.fromWorkSaveDto(model);
        Optional<AppUser> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()) {
            work.setUserId(user.get().getId());
            work.setCreatedBy(user.get().getId());
            work.setStatus(WorkStatus.ACCEPTED);
        } else {
            throw UserException.notFound();
        }
        workRepository.save(work);
    }

    public List<WorkDetailsDto> findAll() {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        return workRepository.findAll()
                .stream()
                .map(work -> {
                    WorkDetailsDto result = mapper.toDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<WorkInfoDto> findAllWorkInfo() {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        return workRepository.findAll()
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<WorkInfoDto> findAllBlockedWorkInfo(String type) {
        List<Work> works;
        if (!Objects.equals(type, "")) {
            WorkType workType = WorkType.valueOf(type.toUpperCase(Locale.ROOT));
            works = workRepository.findAllByTypeEquals(workType);
        } else {
            works = workRepository.findAll();
        }

        works = works
                .stream()
                .filter(x -> x.getStatus() == WorkStatus.BLOCKED)
                .collect(Collectors.toList());

        return mapWorksToDtos(works);
    }

    public List<WorkInfoDto> findWorkInfo(String type, boolean blocked) {
        WorkType workType = WorkType.valueOf(type.toUpperCase(Locale.ROOT));

        List<Work> works = workRepository.findAllByTypeEquals(workType);

        if (!blocked) {
            works = filterBlockedWorks(works);
        }

        return mapWorksToDtos(works);
    }

    public List<WorkInfoDto> findProseWorkInfo(boolean blocked) {
        List<Work> works = workRepository.findAllByTypeIsNotLike(WorkType.POEM);

        if (!blocked) {
            works = filterBlockedWorks(works);
        }

        return mapWorksToDtos(works);
    }

    public WorkDetailsDto findById(Long id, Principal principal) throws WorkException {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Optional<Work> work = workRepository.findById(id);
        WorkDetailsDto result = mapper.toDto(work.orElseThrow(WorkException::notFound));
        Optional<AppUser> caller;
        if (principal != null) {
            caller = userRepository.findByUsername(principal.getName());
        } else {
            caller = Optional.empty();
        }
        if (result.getStatus().equals(WorkStatus.BLOCKED)) {
            if (caller.isEmpty() || !caller.get().getAppUserRole().equals(AppUserRole.MODERATOR)) {
                throw WorkException.accessForbidden();
            }
        }
        userRepository.findById(work.get().getUserId()).ifPresent(appUser -> {
            result.setUsername(appUser.getUsername());
        });
        return result;
    }

    public List<WorkInfoDto> findWorksByUsername(String username) throws UserException {
        List<Work> resultWorks;
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            resultWorks = workRepository.findAllByUserIdEquals(user.get().getId());
        } else {
            throw UserException.notFound();
        }

        return resultWorks
                .stream()
                .filter(work -> work.getStatus() == WorkStatus.ACCEPTED)
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    result.setUsername(username);
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<WorkInfoDto> searchWorks(String query) {
        List<Work> works = workRepository.findAllByTitleContainsIgnoreCaseAndStatusEquals(query, WorkStatus.ACCEPTED);
        return mapWorksToDtos(works);
    }

    public void blockWorkById(Long workId, Principal principal) throws WorkException {
        Optional<Work> work = workRepository.findById(workId);
        if (work.isPresent()) {
            work.get().setStatus(WorkStatus.BLOCKED);
            work.get().setModifiedBy(userRepository.findByUsername(principal.getName()).get().getId());
            workRepository.save(work.get());
        } else {
            throw WorkException.notFound();
        }
    }

    public void unblockWorkById(Long workId, Principal principal) throws WorkException {
        Optional<Work> work = workRepository.findById(workId);
        if (work.isPresent()) {
            work.get().setStatus(WorkStatus.ACCEPTED);
            work.get().setModifiedBy(userRepository.findByUsername(principal.getName()).get().getId());
            workRepository.save(work.get());
        } else {
            throw WorkException.notFound();
        }
    }

    public void getWorkAsPdf(Long workId, HttpServletResponse response) throws IOException, WorkException {
        Optional<Work> work = workRepository.findById(workId);
        if (work.isEmpty()) {
            throw WorkException.notFound();
        }

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + work.get().getTitle() + ".pdf";
        response.setHeader(headerKey, headerValue);

        Document document = Jsoup.parse(convertToHtml("# " + work.get().getTitle() + "\n" + work.get().getText()), "UTF-8");
        document.head().append("""
                <meta charset='UTF-8' /><style>
                            @font-face {
                                font-family: 'source-sans';
                                font-style: normal;
                                font-weight: 300;
                                src: url(../../main/resources/fonts/SourceSans3-Regular.ttf);
                                -fs-font-subset: complete-font;
                            }
                            body {
                                font-family: "source-sans";
                            }
                        </style>""");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (OutputStream os = response.getOutputStream()) {
            String baseUrl = FileSystems.getDefault()
                    .getPath("src/main/resources/")
                    .toUri().toURL().toString();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.toStream(os);
            builder.withW3cDocument(new W3CDom().fromJsoup(document), baseUrl);
            builder.run();
        }

    }

    private String convertToHtml(String text) {
        MutableDataSet options = new MutableDataSet();
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(text);
        return renderer.render(document);
    }

    private List<Work> filterBlockedWorks(List<Work> works) {
        return works
                .stream()
                .filter(w -> w.getStatus().equals(WorkStatus.ACCEPTED))
                .collect(Collectors.toList());
    }

    private List<WorkInfoDto> mapWorksToDtos(List<Work> works) {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);

        return works
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }
}

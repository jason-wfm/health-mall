package com.wechuang.mallshop.sys.controller.front;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.getui.push.v2.sdk.common.ApiException;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.consts.ConstantUpload;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.UploadDto;
import com.wechuang.mallshop.common.utils.*;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.sys.model.entity.MaterialBase;
import com.wechuang.mallshop.sys.model.req.MaterialBaseUploadReq;
import com.wechuang.mallshop.sys.model.res.UploadRes;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.MaterialBaseService;
import com.wechuang.mallshop.sys.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;
import static com.wechuang.mallshop.common.utils.UploadUtil.*;

/**
 * <p>
 * 图片上传 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "图片上传")
@RestController
@RequestMapping("/front/sys/upload")
public class UploadController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private MaterialBaseService materialBaseService;

    @Autowired
    private OssService ossService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Operation(description = "上传文件")
    @PostMapping("/index")
    public CommonRes<UploadRes> upload(MultipartFile upfile, MaterialBaseUploadReq materialBaseUploadReq, HttpServletRequest request) {
        UploadRes uploadRes = null;
        try {
            File upload;
            String dir;

            if (ObjectUtil.isEmpty(upfile)) {
                throw new BusinessException(ResultCode.FAILED);
            }

            InputStream inputStream = upfile.getInputStream();

            String originalFilename = upfile.getOriginalFilename();
            // 获取文件后缀
            String suffix = (originalFilename == null || !originalFilename.contains(".")) ? "" : originalFilename.substring(originalFilename.lastIndexOf("."));
            suffix = suffix.replace(".", "");

            String imageAllowExt = configBaseService.getConfig("upload_image_ext", "");
            String[] imageAllowExtList = imageAllowExt.split(",");

            String vedioAllowExt = configBaseService.getConfig("upload_video_ext", "");
            String[] vedioAllowExtList = vedioAllowExt.split(",");

            String fileAllowExt = configBaseService.getConfig("upload_file_ext", "");
            String[] fileAllowExtList = fileAllowExt.split(",");

            String[] allowExtList = ArrayUtil.addAll(imageAllowExtList, vedioAllowExtList, fileAllowExtList);

            if (!ArrayUtil.contains(allowExtList, suffix)) {
                throw new BusinessException(String.format(__("允许上传格式为：【%s】"), StringUtils.join(allowExtList, ",")));
            }

            switch (materialBaseUploadReq.getMaterialType()) {
                case "image":
                    dir = getUploadImageDir();
                    upload = UploadUtil.uploadImg(upfile, dir, ConstantUpload.UUID_NAME);
                    break;
                case "video":
                    dir = getUploadVideoDir();
                    upload = UploadUtil.uploadVideo(upfile, dir, ConstantUpload.UUID_NAME);
                    break;
                case "document":
                    dir = getUploadFileDir();
                    upload = UploadUtil.uploadFile(upfile, dir, ConstantUpload.UUID_NAME);
                    break;
                default:
                    dir = getUploadFileDir();
                    upload = UploadUtil.upload(upfile, dir, ConstantUpload.UUID_NAME);
                    break;
            }

            String absolutePath = upload.getAbsolutePath();
            String path = absolutePath.replace(File.separator, "/").substring(getUploadBaseDir().length() + 1);
            String requestURL = StrUtil.removeSuffix(request.getRequestURL(), "/index");
            requestURL = ConstantUpload.WEB_URL + "/front/sys/upload";

            String originalName = upfile.getOriginalFilename();

            String materialDuration = "";
            try {
                materialDuration = VideoUtil.getFormatDuration(absolutePath);
            } catch (IOException e) {
                throw new ApiException(String.format("解析音视频时长异常！url【%s】", absolutePath));
            }

            MaterialBase result = new MaterialBase();
            result.setUserId(ContextUtil.getLoginUserId());

            ContextUser loginUser = getLoginUser();

            if (loginUser != null) {
                result.setStoreId(loginUser.getStoreId());
            } else {
                result.setStoreId(0);
            }

            result.setMaterialName(StrUtil.isBlank(originalName) ? upload.getName() : originalName);
            result.setMaterialAlt(result.getMaterialName());
            result.setMaterialSize(upload.length());
            result.setMaterialPath(path);
            result.setGalleryId(materialBaseUploadReq.getGalleryId());
            result.setMaterialType(materialBaseUploadReq.getMaterialType());
            result.setMaterialUrl(requestURL + "/" + path);
            String contentType = UploadUtil.getContentType(upload);
            result.setMaterialMimeType(contentType);
            result.setMaterialDuration(materialDuration);

            if (UploadUtil.isImage(contentType)) {
                //result.setThumbnail(requestURL + "/thumbnail/" + path);
            }

            //result.setDownloadUrl(requestURL + "/download/" + path);

            uploadRes = new UploadRes();
            uploadRes.setUserId(ContextUtil.getLoginUserId());
            uploadRes.setFileName(result.getMaterialName());
            uploadRes.setFilePath(result.getMaterialPath());
            uploadRes.setFileSize(result.getMaterialSize());
            uploadRes.setFileType(result.getMaterialType());
            uploadRes.setMimeType(result.getMaterialMimeType());
            uploadRes.setFileUrl(result.getMaterialUrl());
            uploadRes.setMaterialDuration(materialDuration);

            //oss文件上传网址
            Integer uploadType = configBaseService.getConfig("upload_type", 0);
            if (uploadType.equals(1) || uploadType.equals(2) || uploadType.equals(3)) {
                //针对证书上传，特别处理。
                if (CheckUtil.isNotEmpty(materialBaseUploadReq.getMaterialKey())) {
                    if (materialBaseUploadReq.getMaterialKey().equals("wechat_pay_apiclient_cert")
                            || materialBaseUploadReq.getMaterialKey().equals("wechat_pay_apiclient_key")
                            || materialBaseUploadReq.getMaterialKey().equals("wechat_pay_public_key")
                            || materialBaseUploadReq.getMaterialKey().equals("alipay_app_cert_path")
                            || materialBaseUploadReq.getMaterialKey().equals("alipay_cert_path")
                            || materialBaseUploadReq.getMaterialKey().equals("alipay_root_cert_path")) {
                        uploadRes.setFileUrl(absolutePath);
                    }
                } else {
                    UploadDto uploadDto = new UploadDto();
                    uploadDto.setUploadType(uploadType);
                    uploadDto.setFile(upload);
                    uploadDto.setFileName(result.getMaterialName());
                    uploadDto.setFileSize(result.getMaterialSize());
                    uploadDto.setInputStream(inputStream);
                    uploadDto.setMaterialType(materialBaseUploadReq.getMaterialType());

                    String url = ThirdUtil.upload(uploadDto);
                    //String url = ossService.ossUploadObject(upfile, inputStream, materialBaseUploadReq.getMaterialType());
                    uploadRes.setFileUrl(url);
                    result.setMaterialUrl(url);
                }
            }

            materialBaseService.add(result);

            uploadRes.setUrl(uploadRes.getFileUrl());

            return success(uploadRes);

        } catch (Exception e) {
            LogUtil.error(ConstantLog.UPLOAD, e);
            return fail("上传失败", uploadRes).setError(e.toString());
        }
    }

    @Operation(description = "上传base64文件")
    @PostMapping("/base64")
    public CommonRes<UploadRes> uploadBase64(@RequestParam(name = "base64") String base64, @RequestParam(name = "file_name", required = false) String fileName, MaterialBaseUploadReq materialBaseUploadReq, HttpServletRequest request) {
        MaterialBase result = null;
        UploadRes uploadRes = null;
        try {
            String dir = getUploadBaseDir();
            File upload = UploadUtil.upload(base64, fileName, getUploadImageDir());
            String path = upload.getAbsolutePath().substring(dir.length()).replace("\\", "/");
            String requestURL = StrUtil.removeSuffix(request.getRequestURL(), "/upload/base64");
            requestURL = ConstantUpload.WEB_URL + "/front/sys/upload";

            result = new MaterialBase();
            result.setUserId(ContextUtil.getLoginUserId());
            result.setMaterialName(StrUtil.isBlank(fileName) ? upload.getName() : fileName);
            result.setMaterialSize(upload.length());
            result.setMaterialPath(path);
            result.setGalleryId(materialBaseUploadReq.getGalleryId());
            result.setMaterialType(materialBaseUploadReq.getMaterialType());
            result.setMaterialUrl(requestURL + path);
            //result.setThumbnail(UploadUtil.isImage(upload) ? (requestURL + "/thumbnail" + path) : null);

            uploadRes = new UploadRes();
            uploadRes.setUserId(ContextUtil.getLoginUser().getUserId());
            uploadRes.setFileName(result.getMaterialName());
            uploadRes.setFilePath(result.getMaterialPath());
            uploadRes.setFileSize(result.getMaterialSize());
            uploadRes.setFileType(result.getMaterialType());
            uploadRes.setMimeType(result.getMaterialMimeType());
            uploadRes.setFileUrl(result.getMaterialUrl());

            //oss文件上传网址
            Integer uploadType = configBaseService.getConfig("upload_type", 0);
            if (uploadType.equals(1) || uploadType.equals(2) || uploadType.equals(3)) {
                UploadDto uploadDto = new UploadDto();
                uploadDto.setUploadType(uploadType);
                uploadDto.setFile(upload);
                uploadDto.setFileName(result.getMaterialName());
                uploadDto.setFileSize(result.getMaterialSize());
                uploadDto.setInputStream(new FileInputStream(upload));
                uploadDto.setMaterialType(materialBaseUploadReq.getMaterialType());

                String url = ThirdUtil.upload(uploadDto);
                //String url = ossService.ossUploadObject(upfile, inputStream, materialBaseUploadReq.getMaterialType());
                uploadRes.setFileUrl(url);
                result.setMaterialUrl(url);
            }

            materialBaseService.add(result);

            uploadRes.setUrl(uploadRes.getFileUrl());
            return success(uploadRes);
        } catch (Exception e) {
            LogUtil.error(ConstantLog.UPLOAD, e);
            return fail("上传失败", uploadRes).setError(e.toString());
        }
    }

    @Operation(description = "查看原文件")
    @GetMapping("/{type}/{dir}/{name:.+}")
    public void preview(@PathVariable("type") String type, @PathVariable("dir") String dir, @PathVariable("name") String name,
                        HttpServletResponse response, HttpServletRequest request) {
        File file = new File(getUploadBaseDir(), "/" + type + "/" + dir + "/" + name);
        UploadUtil.preview(file, getPdfOutDir(), ConstantUpload.OPEN_OFFICE_HOME, response, request);
    }

    @Operation(description = "下载原文件")
    @GetMapping("/download/{type}/{dir}/{name:.+}")
    public void download(@PathVariable("type") String type, @PathVariable("dir") String dir, @PathVariable("name") String name,
                         HttpServletResponse response, HttpServletRequest request) {
        String path = "/" + type + "/" + dir + "/" + name;
        MaterialBase record = materialBaseService.get(path);
        File file = new File(getUploadBaseDir(), path);
        String fileName = record == null ? file.getName() : record.getMaterialName();
        UploadUtil.preview(file, true, fileName, null, null, response, request);
    }

    @Operation(description = "查看缩略图")
    @GetMapping("/thumbnail/{type}/{dir}/{name:.+}")
    public void thumbnail(@PathVariable("type") String type, @PathVariable("dir") String dir, @PathVariable("name") String name,
                          HttpServletResponse response, HttpServletRequest request) {
        File file = new File(getUploadBaseDir(), "/" + type + "/" + dir + "/" + name);
        File thumbnail = new File(getUploadSmDir(), "/" + type + "/" + dir + "/" + name);
        UploadUtil.previewThumbnail(file, thumbnail, ConstantUpload.THUMBNAIL_SIZE, response, request);
    }
}


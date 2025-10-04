import React, { useState, useEffect } from 'react';

interface ApiFile {
  id: string;
  displayName: string;
  fileType: string;
}

interface HomeContentProps {
  userId: string;
}

const HomeContent: React.FC<HomeContentProps> = ({ userId }) => {
  const [files, setFiles] = useState<ApiFile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<globalThis.File | null>(null);

  const fetchFiles = async () => {
    try {
      setLoading(true);
      const response = await fetch(`/public/v1/files/${userId}`);
      if (response.ok) {
        const data = await response.json();
        setFiles(data);
      } else {
        setError('Failed to fetch files');
      }
    } catch (error) {
      setError('An error occurred while fetching files');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFiles();
  }, [userId]);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files.length > 0) {
      setSelectedFile(event.target.files[0]);
    }
  };

  const handleUpload = async () => {
    if (!selectedFile) {
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await fetch(`/public/v1/file/upload/${userId}`, {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        fetchFiles();
      } else {
        setError('File upload failed');
      }
    } catch (error) {
      setError('An error occurred during file upload');
    }
  };

  useEffect(() => {
    const fetchFiles = async () => {
      try {
        const response = await fetch(`/public/v1/files/${userId}`);
        if (response.ok) {
          const data = await response.json();
          setFiles(data);
        } else {
          setError('Failed to fetch files');
        }
      } catch (error) {
        setError('An error occurred while fetching files');
      } finally {
        setLoading(false);
      }
    };

    fetchFiles();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      <h2>Files</h2>
      <ul>
        {files.map((file) => (
          <li key={file.id}>
            {file.displayName} ({file.fileType})
          </li>
        ))}
      </ul>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>Upload File</button>
    </div>
  );
};

export default HomeContent;